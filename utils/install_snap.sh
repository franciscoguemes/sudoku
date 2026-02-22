#!/usr/bin/env bash
####################################################################################################
#Args           : [--debug] [--dry-run] [-h|--help]
#Usage          : ./utils/install_snap.sh [--debug] [--dry-run] [-h]
#Output stdout  : Progress messages for each installation step
#Output stderr  : Error messages when a required tool is missing or a step fails
#Return code    : 0 on success, non-zero on failure
#Log file       : /var/log/sudoku/install_snap.log
#Description    : Installs the Sudoku snap package built by utils/build_snap.sh.
#                 The script performs three steps:
#                   1. Verify that a *.snap file exists in target/snap-build/
#                   2. If sudoku is already installed, ask the user whether to
#                      remove it before reinstalling
#                   3. Install the snap with --dangerous --classic flags
#
#Author         : Francisco Güemes
#Email          : francisco@franciscoguemes.com
#See also       : https://snapcraft.io/docs/install-modes
#                 https://snapcraft.io/docs/snap-command
#                 https://stackoverflow.com/questions/192249/how-do-i-parse-command-line-arguments-in-bash
####################################################################################################

# ── Constants ─────────────────────────────────────────────────────────────────
APP_NAME="sudoku"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SCRIPT_NAME="$(basename "${BASH_SOURCE[0]}")"
PROJECT_DIR="$(cd "${SCRIPT_DIR}/.." && pwd)"
LOG_DIR="/var/log/${APP_NAME}"
LOG_FILE="${LOG_DIR}/${SCRIPT_NAME}.log"
SNAP_BUILD_DIR="${PROJECT_DIR}/target/snap-build"
SNAP_APP_NAME="sudoku"

# ── Option defaults ────────────────────────────────────────────────────────────
DEBUG=false
DRY_RUN=false

# ── Usage ──────────────────────────────────────────────────────────────────────
usage() {
    cat <<EOF
Usage: $(basename "$0") [OPTIONS]

Installs the Sudoku snap package from target/snap-build/.
Run utils/build_snap.sh first if no snap file is present.

Options:
  --debug     Enable verbose debug output in the log file
  --dry-run   Print what would be executed without making any changes
  -h, --help  Show this help message and exit
EOF
}

# ── Logging ────────────────────────────────────────────────────────────────────
init_log() {
    mkdir -p "${LOG_DIR}" 2>/dev/null || {
        echo "ERROR: Cannot create log directory ${LOG_DIR}." \
             "Try: sudo mkdir -p ${LOG_DIR} && sudo chown $(whoami) ${LOG_DIR}" >&2
        exit 1
    }
    {
        echo ""
        printf '=%.0s' {1..72}; echo
        echo "  Execution started: $(date '+%Y-%m-%d %H:%M:%S')"
        printf '=%.0s' {1..72}; echo
    } >> "${LOG_FILE}"
}

log() {
    local msg="$1"
    echo "${msg}"
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] ${msg}" >> "${LOG_FILE}"
}

log_debug() {
    [[ "${DEBUG}" == "true" ]] || return 0
    local msg="[DEBUG] $1"
    echo "${msg}"
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] ${msg}" >> "${LOG_FILE}"
}

log_error() {
    local msg="[ERROR] $1"
    echo "${msg}" >&2
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] ${msg}" >> "${LOG_FILE}"
}

# ── Dependency checks ──────────────────────────────────────────────────────────
check_command() {
    local cmd="$1"
    if ! command -v "${cmd}" &>/dev/null; then
        log_error "Required tool '${cmd}' is not installed."
        log_error "Install it with: sudo nala install ${cmd}"
        exit 1
    fi
    log_debug "Found '${cmd}' at: $(command -v "${cmd}")"
}

# ── Command wrapper (honours --dry-run) ────────────────────────────────────────
run() {
    log_debug "Running: $*"
    if [[ "${DRY_RUN}" == "true" ]]; then
        log "[DRY-RUN] Would run: $*"
        return 0
    fi
    "$@"
    local rc=$?
    if [[ ${rc} -ne 0 ]]; then
        log_error "Command failed (exit ${rc}): $*"
        exit "${rc}"
    fi
}

# ── Option parsing ─────────────────────────────────────────────────────────────
parse_options() {
    while [[ $# -gt 0 ]]; do
        case "$1" in
            --debug)
                DEBUG=true
                shift
                ;;
            --dry-run)
                DRY_RUN=true
                shift
                ;;
            -h|--help)
                usage
                exit 0
                ;;
            *)
                log_error "Unknown option: $1"
                usage >&2
                exit 1
                ;;
        esac
    done
}

# ── Main ───────────────────────────────────────────────────────────────────────
main() {
    parse_options "$@"
    init_log

    log "Project directory   : ${PROJECT_DIR}"
    log "Snap build directory: ${SNAP_BUILD_DIR}"
    log "Dry-run : ${DRY_RUN} | Debug : ${DEBUG}"

    check_command snap

    # ── Step 1: Verify snap artifact exists ───────────────────────────────────
    log "Step 1/3 — Looking for snap artifact in ${SNAP_BUILD_DIR}"

    SNAP_FILE="$(ls "${SNAP_BUILD_DIR}"/*.snap 2>/dev/null | head -n 1)"
    if [[ -z "${SNAP_FILE}" ]]; then
        log_error "No *.snap file found in ${SNAP_BUILD_DIR}."
        log_error "Build the snap package first by running: utils/build_snap.sh"
        exit 1
    fi

    log "Found snap artifact: ${SNAP_FILE}"
    log_debug "Snap file size: $(du -sh "${SNAP_FILE}" | cut -f1)"

    # ── Step 2: Check if sudoku is already installed ──────────────────────────
    log "Step 2/3 — Checking if '${SNAP_APP_NAME}' is already installed"

    if snap list "${SNAP_APP_NAME}" &>/dev/null; then
        log "Application '${SNAP_APP_NAME}' is already installed."
        echo ""
        read -r -p "Do you want to remove it and install again? [y/N] " answer
        echo ""

        case "${answer}" in
            [yY]|[yY][eE][sS])
                log "User chose to reinstall. Removing '${SNAP_APP_NAME}'..."
                run sudo snap remove "${SNAP_APP_NAME}"
                log "Application '${SNAP_APP_NAME}' removed successfully."
                ;;
            *)
                log "User chose not to reinstall. Exiting."
                exit 0
                ;;
        esac
    else
        log_debug "Application '${SNAP_APP_NAME}' is not currently installed."
    fi

    # ── Step 3: Install the snap ──────────────────────────────────────────────
    log "Step 3/3 — Installing snap: ${SNAP_FILE}"
    run sudo snap install --dangerous --classic "${SNAP_FILE}"

    log "Done. '${SNAP_APP_NAME}' has been installed successfully."
}

main "$@"

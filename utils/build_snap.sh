#!/usr/bin/env bash
####################################################################################################
#Args           : [--debug] [--dry-run] [-h|--help]
#Usage          : ./utils/build_snap.sh [--debug] [--dry-run] [-h]
#Output stdout  : Progress messages for each build step
#Output stderr  : Error messages when a required tool is missing or a step fails
#Return code    : 0 on success, non-zero on failure
#Log file       : /var/log/sudoku/build_snap.log
#Description    : Builds the Sudoku snap package without polluting the project root.
#                 The build is performed in four steps:
#                   1. Maven clean package   → target/sudoku-*.jar + target/lib/
#                   2. Custom JRE creation   → target/custom-jre/
#                   3. Artifact staging      → target/snap-build/{jre/,jar/,lib/,snap/}
#                   4. snapcraft invocation  → target/snap-build/*.snap
#                 Running snapcraft from target/snap-build/ ensures that all
#                 snapcraft work directories (parts/, stage/, prime/, .snapcraft/)
#                 are created inside target/ and never touch the project root.
#
#Author         : Francisco Güemes
#Email          : francisco@franciscoguemes.com
#See also       : https://snapcraft.io/docs/snapcraft-yaml-reference
#                 https://snapcraft.io/docs/parts-environment-variables
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
APP_JAR_PATTERN="sudoku-*.jar"

# ── Option defaults ────────────────────────────────────────────────────────────
DEBUG=false
DRY_RUN=false

# ── Usage ──────────────────────────────────────────────────────────────────────
usage() {
    cat <<EOF
Usage: $(basename "$0") [OPTIONS]

Builds the Sudoku snap package. All snapcraft work directories are created
inside target/snap-build/ so the project root stays clean.

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

    check_command mvn
    check_command snapcraft

    # ── Step 1: Maven build ───────────────────────────────────────────────────
    log "Step 1/4 — Maven clean package"
    run mvn -f "${PROJECT_DIR}/pom.xml" clean package -DskipTests

    # ── Step 2: Custom JRE ───────────────────────────────────────────────────
    log "Step 2/4 — Create custom JRE"
    run "${SCRIPT_DIR}/create_custom_jre.sh"

    # ── Step 3: Stage artifacts ───────────────────────────────────────────────
    # All paths below are relative to SNAP_BUILD_DIR, matching the source:
    # entries in snap/snapcraft.yaml so that snapcraft can resolve them when
    # invoked from that directory.
    log "Step 3/4 — Stage artifacts into ${SNAP_BUILD_DIR}"

    # snapcraft.yaml + launcher script + sudoku.desktop + icon
    run mkdir -p "${SNAP_BUILD_DIR}/snap/local"
    run mkdir -p "${SNAP_BUILD_DIR}/snap/gui"
    run cp -R "${PROJECT_DIR}/snap/." "${SNAP_BUILD_DIR}/snap/"
    log_debug "Staged: snap/ → snap-build/snap/"

    # Custom JRE  →  snap-build/jre/   (source: jre/ in snapcraft.yaml)
    run mkdir -p "${SNAP_BUILD_DIR}/jre"
    run cp -r "${PROJECT_DIR}/target/custom-jre/." "${SNAP_BUILD_DIR}/jre/"
    log_debug "Staged: target/custom-jre/ → snap-build/jre/"

    # Application JAR  →  snap-build/jar/   (source: jar/ in snapcraft.yaml)
    run mkdir -p "${SNAP_BUILD_DIR}/jar"
    # shellcheck disable=SC2086
    run cp ${PROJECT_DIR}/target/${APP_JAR_PATTERN} "${SNAP_BUILD_DIR}/jar/"
    log_debug "Staged: target/${APP_JAR_PATTERN} → snap-build/jar/"

    # Dependency JARs  →  snap-build/lib/   (source: lib/ in snapcraft.yaml)
    run mkdir -p "${SNAP_BUILD_DIR}/lib"
    run cp -r "${PROJECT_DIR}/target/lib/." "${SNAP_BUILD_DIR}/lib/"
    log_debug "Staged: target/lib/ → snap-build/lib/"

    if [[ "${DEBUG}" == "true" ]]; then
        log_debug "snap-build/ tree (depth 2):"
        find "${SNAP_BUILD_DIR}" -maxdepth 2 | sort >> "${LOG_FILE}"
    fi

    # ── Step 4: Build snap ────────────────────────────────────────────────────
    log "Step 4/4 — Run snapcraft from ${SNAP_BUILD_DIR}"
    if [[ "${DRY_RUN}" == "true" ]]; then
        log "[DRY-RUN] Would run: cd ${SNAP_BUILD_DIR} && snapcraft --destructive-mode"
    else
        (cd "${SNAP_BUILD_DIR}" && snapcraft --destructive-mode)
        local rc=$?
        if [[ ${rc} -ne 0 ]]; then
            log_error "snapcraft exited with code ${rc}"
            exit "${rc}"
        fi
    fi

    log "Done. Snap artifact: ${SNAP_BUILD_DIR}/*.snap"
}

main "$@"

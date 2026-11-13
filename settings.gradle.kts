// Repository resolution has two modes:
// - Normal machines: the public google() / mavenCentral() / gradlePluginPortal().
// - The author's build VM: its egress proxy resets Java TLS connections, so
//   when SCRIPTLET_LOCAL_MIRROR=1 is set in the environment, dependencies are
//   resolved through a local curl-backed mirror instead (see build.sh, which
//   is a VM-local helper and is not part of this repository).
val localMirror = System.getenv("SCRIPTLET_LOCAL_MIRROR") == "1"

pluginManagement {
    repositories {
        if (System.getenv("SCRIPTLET_LOCAL_MIRROR") == "1") {
            maven { url = uri("http://127.0.0.1:18080/google"); isAllowInsecureProtocol = true }
            maven { url = uri("http://127.0.0.1:18080/central"); isAllowInsecureProtocol = true }
            maven { url = uri("http://127.0.0.1:18080/plugins"); isAllowInsecureProtocol = true }
        } else {
            google()
            mavenCentral()
            gradlePluginPortal()
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        if (System.getenv("SCRIPTLET_LOCAL_MIRROR") == "1") {
            maven { url = uri("http://127.0.0.1:18080/google"); isAllowInsecureProtocol = true }
            maven { url = uri("http://127.0.0.1:18080/central"); isAllowInsecureProtocol = true }
        } else {
            google()
            mavenCentral()
        }
    }
}
rootProject.name = "Scriptlet"
include(":app")

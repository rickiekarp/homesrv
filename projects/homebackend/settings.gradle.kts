rootProject.name = "homebackend"

include("Foundation")
include("HomeServer")
include("LoginServer")

for (project in rootProject.children) {
    project.apply {
        buildFileName = "build.gradle"
    }
}
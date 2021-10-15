
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.maven

/**
 * @description:
 * @author lyb
 * @date 2021年07月25日 9:59 下午
 */

fun RepositoryHandler.addRepositories() {
    maven("https://jitpack.io")
    mavenCentral()
    google()
    jcenter()
}
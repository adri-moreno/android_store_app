plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-test-fixtures`
}

dependencies {
    implementation(libs.coroutines.core)

    // Test
    testImplementation(libs.bundles.test.unit)

    // Test Fixtures
    testFixturesImplementation(libs.coroutines.test)
    testFixturesImplementation(libs.junit)
}

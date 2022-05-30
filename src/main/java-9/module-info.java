module com.github.pemistahl.lingua {
    exports com.github.pemistahl.lingua.api;

    requires kotlin.stdlib;

    requires it.unimi.dsi.fastutil;
    requires okio;

    // Moshi accesses JSON serializer using reflection; must open the package
    // TODO: Once new Moshi version has module names, change it to `opens ... to com.squareup.moshi`
    //       and comment in the `requires` declarations below
    opens com.github.pemistahl.lingua.internal;
    // requires com.squareup.moshi;
    // requires com.squareup.moshi.kotlin;
}

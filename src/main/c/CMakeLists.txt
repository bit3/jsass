cmake_minimum_required(VERSION 3.2)
project(jsass)

set(SOURCE_FILES io_bit3_jsass_adapter_NativeAdapter.c empty.cpp)

include_directories(
    # apple
    "/System/Library/Frameworks/JavaVM.framework/Headers"

    # unix
    "/usr/lib/jvm/java/include"
    "/usr/lib/jvm/java/include/linux"
    "/usr/lib/jvm/default/include"
    "/usr/lib/jvm/default/include/linux"
    "/usr/lib/jvm/default-java/include"
    "/usr/lib/jvm/default-java/include/linux"
    "/usr/lib/jvm/java-8-openjdk-amd64/include"
    "/usr/lib/jvm/java-8-openjdk-amd64/include/linux"
    "/usr/lib/jvm/java-8-oracle/include"
    "/usr/lib/jvm/java-8-oracle/include/linux"

    # libsass
    "../libsass/include"
)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++0x")

message("search for libsass static library in: ${CMAKE_SOURCE_DIR}/../libsass/lib")
find_library(SASS_EXT_LIBRARY NAMES sass libsass PATHS "${CMAKE_SOURCE_DIR}/../libsass/lib" NO_DEFAULT_PATH)
message(STATUS "libsass library: ${SASS_EXT_LIBRARY}")
add_library(sass UNKNOWN IMPORTED)
set_property(TARGET sass PROPERTY IMPORTED_LOCATION "${SASS_EXT_LIBRARY}")

file(WRITE empty.cpp "")

add_library(jsass SHARED ${SOURCE_FILES})
set_property(TARGET jsass PROPERTY C_STANDARD 99)

set(LINK_FLAGS ${LINK_FLAGS} "-Wl,--whole-archive")

if(APPLE)
    target_link_libraries(jsass sass)
else()
    target_link_libraries(jsass -static-libgcc -static-libstdc++ "-Wl,--whole-archive" sass "-Wl,--no-whole-archive")
endif()

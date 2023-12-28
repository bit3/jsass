cmake_minimum_required(VERSION 3.21)
project(jsass)

set(SOURCE_FILES io_bit3_jsass_adapter_NativeAdapter.c empty.cpp)

include_directories(
    # apple
    "/System/Library/Frameworks/JavaVM.framework/Headers"

    # osxcross
    "/osxcross/include/tapi"
    "/osxcross/include/xar"
    "/usr/include/clang/14/include/"

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

    # custom installation
    "/opt/java8/include"
    "/opt/java8/include/linux"
    "/opt/java11/include"
    "/opt/java11/include/linux"
    "/opt/java17/include"
    "/opt/java17/include/linux"
    "/opt/java21/include"
    "/opt/java21/include/linux"

    # libsass
    "../libsass/include"
)
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -fvisibility=hidden")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++0x -fvisibility=hidden")
set(CMAKE_C_VISIBILITY_PRESET hidden)
set(CMAKE_CXX_VISIBILITY_PRESET hidden)

message("search for libsass static library in: ${CMAKE_SOURCE_DIR}/../libsass/lib")
find_library(SASS_EXT_LIBRARY NAMES sass libsass PATHS "${CMAKE_SOURCE_DIR}/../libsass/lib" NO_DEFAULT_PATH)
message(STATUS "libsass library: ${SASS_EXT_LIBRARY}")
add_library(sass STATIC IMPORTED)
set_property(TARGET sass PROPERTY IMPORTED_LOCATION "${SASS_EXT_LIBRARY}")

file(WRITE empty.cpp "")

add_library(jsass SHARED ${SOURCE_FILES})
set_property(TARGET jsass PROPERTY C_STANDARD 99)

set(LINK_FLAGS ${LINK_FLAGS} -static-libgcc -static-libstdc++ "-Wl,--no-whole-archive")

if(APPLE)
    target_link_libraries(jsass PUBLIC sass "-exported_symbols_list ${CMAKE_SOURCE_DIR}/exported_symbols_list.txt")
else()
    target_link_libraries(jsass PUBLIC sass "-Wl,--version-script=${CMAKE_SOURCE_DIR}/version_script.map")
endif()

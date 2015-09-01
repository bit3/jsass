cmake_minimum_required(VERSION 3.2)
project(jsass)

set(SOURCE_FILES io_bit3_jsass_adapter_NativeAdapter.c empty.cpp)

include_directories(
    # docker
    "/opt/jdk1.8.0_60/include"
    "/opt/jdk1.8.0_60/include/linux"

    # apple
    "/System/Library/Frameworks/JavaVM.framework/Headers"

    # unix
    "/usr/lib/jvm/default-java/include"
    "/usr/lib/jvm/java-8-oracle/include"
    "/usr/lib/jvm/java-8-oracle/include/linux"

    # libsass
    "../libsass"
)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++0x")

find_library(SASS_EXT_LIBRARY PATHS "${CMAKE_SOURCE_DIR}/../libsass/lib" NAMES sass)
message(STATUS "libsass library: ${SASS_EXT_LIBRARY}")
add_library(sass UNKNOWN IMPORTED)
set_property(TARGET sass PROPERTY IMPORTED_LOCATION "${SASS_EXT_LIBRARY}")

file(WRITE empty.cpp "")

add_library(jsass SHARED ${SOURCE_FILES})
set_property(TARGET jsass PROPERTY C_STANDARD 99)
set(LINK_FLAGS ${LINK_FLAGS} "-Wl,--whole-archive")

if(APPLE)
    target_link_libraries(jsass sass)
elseif(WIN32)
    target_link_libraries(jsass "-Wl,--whole-archive" sass "-Wl,--no-whole-archive")
else()
    target_link_libraries(jsass "-Wl,--whole-archive" sass "-Wl,--no-whole-archive")
endif()

cmake_minimum_required(VERSION 3.0)
project(jsass)

include_directories(
    "/usr/lib/jvm/java-8-oracle/include"
    "/usr/lib/jvm/java-8-oracle/include/linux"
    "../libsass"
)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

file(COPY ${CMAKE_SOURCE_DIR}/../libsass/lib/libsass.so DESTINATION ${CMAKE_SOURCE_DIR})

add_library(sass SHARED IMPORTED)
SET_TARGET_PROPERTIES(sass PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/libsass.so)

set(SOURCE_FILES io_bit3_jsass_adapter_NativeAdapter.c)
add_library(jsass SHARED ${SOURCE_FILES})
set_property(TARGET jsass PROPERTY C_STANDARD 99)
target_link_libraries(jsass sass)

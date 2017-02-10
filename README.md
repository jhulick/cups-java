# CupsJava
After frustrastions trying the existing Cups4j and IPP java libs not providing security I needed, I created this update to the core cups4j project. It only includes basic TLS handling and IPP with basic authentication, which was sufficient for my needs.  

### Print Service Usage

Print service works through the `CupsClient.java` API

* `CupsService.java`: handles print jobs management

# Contribute

This library wasn't widely tested. If you find bugs, either submit a new issue or fork/fix/submit PR.

Please use the `develop` branch for all testing and troubleshooting.

## External libraries

* A modified version of cups4j 0.63. The original source code and further details about cups4j may be found at http://www.cups4j.org/ (licensed under the LGPL license)

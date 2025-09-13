set quiet

default:
  just --list

local:
  mvn hpi:run

package:
  mvn spotless:apply install

package-release:
  mvn spotless:apply install \
    -Dchangelist=${CHANGELIST:-} \
    -Dno-incrementals

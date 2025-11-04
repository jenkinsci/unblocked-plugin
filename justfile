set quiet

default:
  just --list

local:
  mvn hpi:run

package:
  mvn spotless:apply install

package-java11:
  mvn spotless:apply hpi:hpi

package-release:
  mvn spotless:apply install \
    -Dchangelist=${CHANGELIST:-} \
    -Dno-incrementals

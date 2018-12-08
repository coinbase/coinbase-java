#!/bin/sh

PRE_COMMIT_FILE_NAME=pre-commit
HOOKS_PATH=.git/hooks
PRE_COMMIT_FILE=${HOOKS_PATH}/${PRE_COMMIT_FILE_NAME}

if [ -f ${PRE_COMMIT_FILE} ] ; then
    echo 'Pre-commit script already exists, deleting it'
    rm ${PRE_COMMIT_FILE}
fi

touch ${PRE_COMMIT_FILE}
chmod 755 ${PRE_COMMIT_FILE}

echo '#!/bin/sh

echo "Running pre-commit script"

./gradlew :coinbase-java:codeQualityCheck' > ${PRE_COMMIT_FILE}

echo 'Pre-commit script is set at '${PRE_COMMIT_FILE}
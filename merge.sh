#! /bin/bash
# Merge script
GIT_USER="$1"
GIT_PASS="$2"

# Specify the development branch and stable branch names
FROM_BRANCH="$3"
TO_BRANCH="$4"

# Get the current branch
export PAGER=cat
CURRENT_BRANCH=development
echo "current branch is '$CURRENT_BRANCH'"

# Create the URL to push merge to
URL=github.com/AlmiSelus/AwesomeProject.git
echo "Repo url is $URL"
PUSH_URL="https://$GIT_USER:$GIT_PASS@$URL"

git config user.email "JanuszCebulaJ@gmail.com"
git config user.name "JanuszCebulaJ"
git config user.password "OnionJava"
git config --global merge.ours.driver true

if [ "$CURRENT_BRANCH" = "$FROM_BRANCH" ] ; then
    # Checkout the FROM branch
    #git checkout $FROM_BRANCH && \
    #echo "Checking out $TO_BRANCH..." && \

    # Checkout the latest TO branch
    git fetch origin $TO_BRANCH:$TO_BRANCH && \
    git checkout $TO_BRANCH && \

    # Merge the dev into latest stable
    echo "Merging changes..." && \
    git merge $FROM_BRANCH -m "Merge branches (development->master) for build $TRAVIS_COMMIT"&& \

    # Push changes back to remote vcs
    echo "Pushing changes..." && \
    git push $PUSH_URL && \
    echo "Merge complete!" || \
    echo "Error Occurred. Merge failed"
else
    echo "Not on $FROM_BRANCH. Skipping merge"
fi
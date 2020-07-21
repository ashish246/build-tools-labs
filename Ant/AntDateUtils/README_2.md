Ant GIT tasks
=============

In general all GIT-related ant targets are executed on the project repository as well as on all repositories in sources.

GIT pull
--------

* `ant git-pull` will run a git-pull from the origin remote
* Option `-Dremote=NAME` will run a git-pull from the remote NAME

GIT push
--------

* `ant git-push` will run a git-push on all repositories to the origin remote 
* Option `-Dremote=NAME` will run a git-push on all repositories to the remote NAME 
* Option `-Ddryrun=true` only simulates a push to remote but does not change anything
* Option `-Dremotebranch=NAME` push the current branch to a _differntly_ named remote branch, if not provided the remote branch name is the same as the current local branch name 


GIT branch
------------

* `ant git-branch` performs local and remote branch-specific actions
* Option `-Dremove=NAME` delete the branch NAME locally, if this is the current branch the script execution will stop
* Option `-Dremoteremove=NAME` delete the branch NAME remotely
* Option `-Dremote=NAME` execute the branch action on the remote NAME, default is _origin_


GIT checkout
------------

* `ant git-checkout -Dbranch=NAME` will checkout/switch to the branch NAME; if the branch does not exists the process will create a new branch NAME after a user interaction dialog


GIT tag
-------

* `ant git-tag` perform tag-specific actions
* Option `-Dcreate=NAME` will create a new tag NAME, if the tag already exists the script will stop with an error message 
* Option `-Ddelete=NAME` delete a tag NAME from the repositories

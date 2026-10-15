_This document is a short tutorial about the Git tool._

# Preface

Git is a [version control system (VCS)](https://en.wikipedia.org/wiki/Version_control), which is a tool aimed at tracking code changes. It allows several people to work on the same code without losing anyone's work, to keep a history of modifications and to revert to previous versions, and much more. It is an industry-standard that almost every developer uses (except the rebels at Google), and powers platforms such as GitHub or GitLab.

# Overview

_NOTE: the following is a simplified view which does not perfectly model Git's inner workings, but is sufficient to get started. You can find more in-depth guides online (yes, Git is a very complex topic, and this guide only scratches the surface. However, understanding it fully will already give you a better comprehension than many developers have)._

## Commits

Git keeps track of a sequence of _commits_, which encode a _diff_: the code changes between before and after the commit. Thus, Git does __not__ store the full code as-is, it can only rebuild it by looking at the history of all commits.

Here's an example of diff:

```python
# Before
text = "I am a sausage."
print(text)
```

```python
# After
text = "I am a sausage."
print("Hello, World!")
print(text)
```

Git would generate the following diff:

```diff
~/dev/git-test $ git diff
diff --git a/a.py b/a.py
index bdebfe6..6806c1f 100644
--- a/a.py
+++ b/a.py
@@ -1,3 +1,4 @@
-# Before
+# After
 text = "I am a sausage."
+print("Hello, World!")
 print(text)
~/dev/git-test $
```

The interesting part is the lines highlighted in red (indicating a line got deleted) and in green (indicating a line got added). If a line gets modified, Git interprets it as if the previous line got removed and the new (modified) line got inserted in the same place.

The `git log` command allows us to view the history of all commits (which you can also view by clicking "Commits" or "History" in GitHub/GitLab).

```yaml
~/dev/git-test $ git log
commit 6a5474f733c9e128ab5d0b8b1dc4fe22057601ff (HEAD -> master)
Author: Thibault M <thibaultmonni@gmail.com>
Date:   Tue Oct 13 15:19:10 2026 +0200

    Add hello world print

commit 5e4fd274b084026df19a87024213e1cb4abc8fac
Author: Thibault M <thibaultmonni@gmail.com>
Date:   Tue Oct 13 14:24:05 2026 +0200

    First commit
~/dev/git-test $
```

Here, "Add hello world print" and "First commit" are called the _commit message_, which act as a title to each commit. Moreover, "6a5474f733c9e128ab5d0b8b1dc4fe22057601ff" and "5e4fd274b084026df19a87024213e1cb4abc8fac" are called the _commit hash_, which are unique identifiers associated with each commit.

## Remotes

Git assigns a list of _remotes_ to each repository.

```console
~/dev/frites-2027 $ git remote -v
origin  https://gitlab.com/ftc-civ/frites/2027.git (fetch)
origin  https://gitlab.com/ftc-civ/frites/2027.git (push)
~/dev/frites-2027 $
```

Here, my local clone has the remote (_upstream_) GitLab repository marked as _origin_. This allows me to sync my local clone to the repository stored on GitLab:

```console
~/dev/frites-2027 $ git status
On branch main
Your branch is behind 'origin/main' by 2 commits, and can be fast-forwarded.
  (use "git pull" to update your local branch)

nothing to commit, working tree clean
~/dev/frites-2027 $ git pull
Updating 4c24407..6b2b06b
Fast-forward
 docs/getting-started/README.md          |  4 +++-
 docs/getting-started/ftc-programming.md | 79 ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++---------
 docs/getting-started/git.md             | 45 +++++++++++++++++++++++++++++++++++++++++++++
 3 files changed, 118 insertions(+), 10 deletions(-)
 create mode 100644 docs/getting-started/git.md
~/dev/frites-2027 $
```

Here, `git status` informs us that the remote repository (GitLab) has the same commit history as our local clone, but with 2 additional commits at the end. Note that this command does not do any request to the remote over the Internet, so it may not know if commits have been added. Thus, you might have to run `git fetch` to allow Git to know the local clone is behind:

```console
~/dev/frites-2027 $ git status
On branch main
Your branch is up to date with 'origin/main'.

nothing to commit, working tree clean
~/dev/frites-2027 $ git fetch
remote: Enumerating objects: 41, done.
remote: Counting objects: 100% (41/41), done.
remote: Compressing objects: 100% (21/21), done.
remote: Total 22 (delta 13), reused 3 (delta 0), pack-reused 0 (from 0)
Unpacking objects: 100% (22/22), 5.45 KiB | 372.00 KiB/s, done.
From https://gitlab.com/ftc-civ/frites/2027
   6b2b06b..bad6969  main       -> origin/main
~/dev/frites-2027 $ git status
On branch main
Your branch is behind 'origin/main' by 2 commits, and can be fast-forwarded.
  (use "git pull" to update your local branch)

nothing to commit, working tree clean
~/dev/frites-2027 $
```

The `git pull` command allows us to update the local repository: Git simply takes the diffs of the additional commits from the remote and applies them to the local repository in order.

If, instead, your local repository is _ahead_ (that is, it has commits that the remote does not yet have), you can run `git push` to sync the remote to your local changes (so, this is the same as `git pull` but the other way around).

## Merge conflicts

_Let's be clear. This is the worst nightmare for anyone getting started with Git. If you get a merge conflict, don't cry: just close your code editor and never open it again._

A merge conflict occurs when your local repository is both ahead and behind the remote: we call this a _divergence_.

```console
~/dev/frites-2027 $ git log origin main --graph --oneline -n 5
* 10f4667 (HEAD -> main) Divergent commit
| * bad6969 (origin/main, origin/HEAD) Implement PID velocity control for field centric movement
| * 4d644c7 Extend git guide in doc/
|/
* 6b2b06b Start a git tutorial in docs/
* e50f1bc Extend & reformat docs
~/dev/frites-2027 $
```

In this scenario, the local repository is ahead (indicated by the "HEAD" annotation) one commit: 4effc4a, and behind (indicated by the "origin/HEAD" annotation) two commits: 4d644c7 then bad6969.

### Merging with no conflicts

There are several ways to fix this divergence. In many cases, it will require a single command and you will be done.

```console
~/dev/frites-2027 $ git log origin main --graph --oneline -n 5
* 5eaf4fc (HEAD -> main) Divergent commit without conflicts
| * bad6969 (origin/main, origin/HEAD) Implement PID velocity control for field centric movement
| * 4d644c7 Extend git guide in doc/
|/
* 6b2b06b Start a git tutorial in docs/
* e50f1bc Extend & reformat docs
~/dev/frites-2027 $ git rebase
Successfully rebased and updated refs/heads/main.
~/dev/frites-2027 $ git status
On branch main
Your branch is ahead of 'origin/main' by 1 commit.
  (use "git push" to publish your local commits)

nothing to commit, working tree clean
~/dev/frites-2027 $
```

The `git rebase` (or equivalently, `git pull --rebase` which also fetches before rebasing, whereas `git rebase` operates offline similarly to `git status`) managed to merge the changes without any issues, yielding the following history, said _linear_:

```console
~/dev/frites-2027 $ git log --oneline -n 5
0a992ed (HEAD -> main) Divergent commit without conflicts
bad6969 (origin/main, origin/HEAD) Implement PID velocity control for field centric movement
4d644c7 Extend git guide in doc/
6b2b06b Start a git tutorial in docs/
e50f1bc Extend & reformat docs
~/dev/frites-2027 $ git fetch && git status
On branch main
Your branch is ahead of 'origin/main' by 1 commit.
  (use "git push" to publish your local commits)

nothing to commit, working tree clean
~/dev/frites-2027 $
```

And the divergence is solved!

### Merging with conflicts

In other cases, when your local commits and the remote commits modified the same part of the code, Git does not know how to proceed and asks you to _resolve conflicts_. Now _this_ is the somewhat scary part.

> TODO: finish this section.

### Rebase vs Merge

> TODO: finish this section.

_This document is a short tutorial about the Git tool._

# Preface

Git is a [version control system (VCS)](https://en.wikipedia.org/wiki/Version_control), which is a tool aimed at tracking code changes. It allows several people to work on the same code without losing anyone's work, to keep a history of modifications and to revert to previous versions, and much more. It is an industry-standard that almost every developer uses (except the rebels at Google), and powers platforms such as Github or Gitlab.

# Overview

_NOTE: the following is a simplified view which does not perfectly model Git's inner workings, but is sufficient to get started. You can find more in-depth guides online (yes, Git is a very complex topic, and this guide only scratches the surface. However, undestanding it fully will already give you a better comprehension than many developers have)._

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

The `git log` command allows us to view the history of all commits (which you can also view by clicking "Commits" or "History" in Github/Gitlab).

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

Here, "Add hello world print" and "First commit" are called the _commit message_, which act as a title to each commit. Moreover, "6a5474f733c9e128ab5d0b8b1dc4fe22057601ff" and "5e4fd274b084026df19a87024213e1cb4abc8fac" are called the _commit hash_, which are unique identifiers associated to each commit.

## Remotes

Git assigns a list of _remotes_ to each repository.

```console
~/dev/frites-2027 $ git remote -v
origin  https://gitlab.com/ftc-civ/frites/2027.git (fetch)
origin  https://gitlab.com/ftc-civ/frites/2027.git (push)
~/dev/frites-2027 $
```

Here, my local clone has the remote (_upstream_) Gitlab repository marked as _origin_. This allows me to sync my local clone to the repository stored on Gitlab:

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

Here, `git status` informs us that the remote repository (Gitlab) has the same commit history as our local clone, but with 2 additional commits at the end. Note that this command does not do any request to the remote over the Internet, so it may not know if commits have been added. Thus, you might have to run `git fetch` to allow Git to know the local clone is behind.

The `git pull` command allows us to update the local repository: Git simply takes the diffs of the additional commits from the remote and applies them to the local repository in order.

If, instead, your local repository is _ahead_ (that is, it has commits that the remote does not yet have), you can run `git push` to sync the remote to your local changes (so, this is the same as `git pull` but the other way around).

### Merge conflicts

_Let's be clear. This is the worst nightmare for anyone getting started with Git. If you get a merge conflict, don't cry: just close your code editor and never open it again._

A merge conflict occurs when your local repository is both ahead and behind the remote: we call this a _divergence_.

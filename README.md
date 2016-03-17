[![Codacy Badge](https://api.codacy.com/project/badge/grade/6d53ef77cc284e4bbe1bf65b6a4e1a56)](https://www.codacy.com/app/Codacy/codacy-find-sec-bugs)
[![Circle CI](https://circleci.com/gh/codacy/codacy-find-sec-bugs.svg?style=svg)](https://circleci.com/gh/codacy/codacy-find-sec-bugs)
# Codacy Find Security Bugs

This is the docker engine we use at Codacy to have [Find Security Bugs](http://find-sec-bugs.github.io/) support.
You can also create a docker to integrate the tool and language of your choice!
Check the **Docs** section for more information.

## Usage

You can create the docker by doing:

```
sbt docker:publishLocal
```

The docker is ran with the following command:

```
docker run -it -v $srcDir:/src  <DOCKER_NAME>:<DOCKER_VERSION>
```

## Docs

[Tool Developer Guide](http://docs.codacy.com/v1.5/docs/tool-developer-guide)

[Tool Developer Guide - Using Scala](http://docs.codacy.com/v1.5/docs/tool-developer-guide-using-scala)

## Test

We use the [codacy-plugins-test](https://github.com/codacy/codacy-plugins-test) to test our external tools integration.
You can follow the instructions there to make sure your tool is working as expected.

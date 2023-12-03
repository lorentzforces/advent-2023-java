SHELL := bash
.ONESHELL:
.SHELLFLAGS := -eu -o pipefail -c
.DELETE_ON_ERROR:
MAKEFLAGS += --warn-undefined-variables
MAKEFLAGS += --no-builtin-rules

ifeq ($(origin .RECIPEPREFIX), undefined)
  $(error This Make does not support .RECIPEPREFIX. Please use GNU Make 4.0 or later)
endif
.RECIPEPREFIX = >

# We use gradle for compilation, so we don't really have to do staleness detection. (most things
# will be phony)

build:
> ./gradlew build
.PHONY: build

run:
> ./gradlew run
.PHONY: run

test:
> ./gradlew test
.PHONY: test

# Upgrade Plan

## Metadata

- Session ID: `local-java21-refactor-20260313`
- Project: `java21-springboot-demo`
- Generated: `2026-03-13`
- Current branch: `main`
- Current commit: `c1c6bf8ad500f2220d481b01128cec5aa028cf8c`
- Warning: Internal upgrade telemetry tools were unavailable in this environment, so this run is being tracked locally.

## Guidelines

- Refactor the current structure into feature-oriented packages instead of a single controller-centric demo.
- Add proper, runnable examples for the requested Java 21 topics.
- Keep changes minimal outside the Java 21 demo project.
- Prefer standard Java 21 APIs first; note incubator or preview requirements explicitly where applicable.

## Options

- Run tests before and after the upgrade: true
- Preferred build entrypoint: `mvn`

## Available Tools

| Tool          | Status      | Notes                                                                   |
| ------------- | ----------- | ----------------------------------------------------------------------- |
| JDK 17        | Available   | Current default `java -version` output shows JDK 17                     |
| JDK 21        | Required    | Must be located or installed before successful build/runtime validation |
| Maven         | Available   | `mvn` command is installed                                              |
| Maven Wrapper | Not present | Project currently uses system Maven                                     |

## Technology Stack

| Area        | Current                                    | Target                       | Notes                                                                |
| ----------- | ------------------------------------------ | ---------------------------- | -------------------------------------------------------------------- |
| Java        | 21 in `pom.xml`, JDK 17 on machine default | Java 21 runtime and compiler | Build validation requires an actual JDK 21 installation              |
| Spring Boot | 3.4.2                                      | 3.4.2                        | Already compatible with Java 21                                      |
| Web         | Spring MVC + WebFlux                       | Spring MVC + WebFlux         | Use MVC for feature endpoints and WebFlux only where streaming helps |
| Testing     | Spring Boot Starter Test                   | Spring Boot Starter Test     | Add focused controller/service tests                                 |

## Derived Upgrades

| Topic                   | Current State                  | Planned Change                                                                                                         |
| ----------------------- | ------------------------------ | ---------------------------------------------------------------------------------------------------------------------- |
| Virtual Threads         | Basic endpoint only            | Move logic to a dedicated service with comparison response DTOs                                                        |
| Structured Concurrency  | Documented but not implemented | Add a dedicated example service and endpoint; handle incubator availability explicitly                                 |
| Pattern Matching Switch | Basic sealed-type switch       | Expand to domain examples with clearer payload mapping                                                                 |
| Record Patterns         | Missing                        | Add nested record-based order analysis example                                                                         |
| Sequenced Collections   | Missing                        | Add collection ordering demo using Java 21 interfaces                                                                  |
| String Templates        | Missing                        | Add example behind a preview-compatible service or provide documented fallback if build cannot enable preview globally |
| Scoped Values           | Missing                        | Add request-context propagation example with explicit threading boundary                                               |
| Foreign Memory API      | Missing                        | Add safe memory segment example in a service layer                                                                     |
| Generational ZGC        | Missing                        | Add operational guidance endpoint/documentation rather than fake runtime behavior                                      |

## Key Challenges

- Java 21 language and library features are partially split across standard, incubator, and preview tracks; the build must distinguish those cases clearly.
- The workstation currently defaults to JDK 17, so code changes alone are insufficient until JDK 21 is available for validation.
- String Templates are still preview in Java 21, which affects compiler/plugin configuration and can complicate the rest of the project if enabled globally.
- Structured Concurrency and Scoped Values in Java 21 require incubator modules, so examples must either compile with explicit module flags or be isolated carefully.

## Upgrade Steps

| Step | Title                                   | Goal                                              | Planned Changes                                                                                                   | Verification                                                  |
| ---- | --------------------------------------- | ------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------- |
| 1    | Setup Environment                       | Prepare a Java 21-capable build path              | Locate or install JDK 21 and document the runtime/build command to use                                            | `java -version`, `mvn -version`, `mvn -q -DskipTests compile` |
| 2    | Setup Baseline                          | Capture current project behavior                  | Run baseline compile/tests and record current failures or environment blockers                                    | `mvn -q clean test`                                           |
| 3    | Restructure Demo Architecture           | Replace the monolithic demo controller shape      | Introduce feature packages, DTOs, and services for cleaner examples                                               | `mvn -q test-compile`                                         |
| 4    | Implement Standard Java 21 Features     | Add fully supported Java 21 examples              | Add virtual threads, pattern matching switch, record patterns, sequenced collections, and foreign memory examples | `mvn -q test-compile`                                         |
| 5    | Implement Incubator or Preview Features | Add examples that need explicit flags             | Add structured concurrency, scoped values, and string templates with explicit build/runtime configuration         | `mvn -q test-compile` with required flags                     |
| 6    | Improve API and Documentation           | Make the examples discoverable and usable         | Add endpoint catalog, runtime notes, and a Generational ZGC operational guide                                     | `mvn -q test-compile`                                         |
| 7    | Final Validation                        | Confirm the refactor and examples work end-to-end | Run compile/tests on Java 21 and fix any failures                                                                 | `mvn -q clean test`                                           |

## Plan Review

- The requested features are all covered, but three of them have non-default runtime implications in Java 21: Structured Concurrency, Scoped Values, and String Templates.
- Generational ZGC is a JVM/runtime capability, not an application code feature, so the plan treats it as an operational example plus documentation and startup guidance.
- Successful validation depends on making JDK 21 available in this environment.

#!/usr/bin/env bun
import { $ } from "bun";

$.nothrow();
await $`bun build.ts`
await $`rm ../webapp/*`
await $`cp dist/* ../resources/webapp`
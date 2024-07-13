#!/usr/bin/env bun
import { $ } from "bun";

$.nothrow();
await $`build.ts`
await $`rm ../webapp/*`
await $`cp dist/* ../webapp`
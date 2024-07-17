#!/usr/bin/env bun
import { $ } from "bun";

$.nothrow();
await $`rm -r .parcel-cache`
await $`rm -r dist`
await $`bun run parcel:build`


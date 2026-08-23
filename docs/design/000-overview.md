# 000 — Overview

## What this is

`streams` is a distributed stream processing engine written from scratch in
Java. It processes unbounded streams of records with correct event-time
semantics and survives machine failure without losing or duplicating results.

The project covers the whole stack rather than one layer: the engine that runs
dataflow jobs, and the control plane that schedules, checkpoints, and recovers
them.

## Why

To understand how systems like Flink and Kafka Streams work underneath. The
interesting problems in stream processing are not in the operators — they are
in time, state, and failure:

- Records arrive late and out of order, so "this window is complete" is a
  judgement call, not a fact.
- Operators hold state that must survive a crash without recomputing from the
  beginning of the stream.
- A snapshot of a distributed system has to be taken without stopping it.

Reading about these is not the same as having to make them work. Building the
engine is the point.

## Structure

| Module | Owns |
| --- | --- |
| `streams-api` | The user-facing job description. Knows nothing about execution. |
| `streams-runtime` | The data plane: operator chain, event time, state, shuffle. |
| `streams-master` | The control plane: scheduling, membership, checkpoint coordination. |
| `streams-core` | Config, shared types, utilities. |
| `streams-examples` | Runnable jobs used to exercise the system. |

Dependencies run one way only:

```
streams-examples -> streams-runtime -> streams-api -> streams-core
streams-master   -> streams-core
```

Gradle's `api` and `implementation` scopes enforce this. A module cannot reach a
transitive dependency it did not declare, so layering violations are compile
errors rather than review comments.

**API** — builds a logical job description and hands it to the runtime.

**Data plane** — executes the dataflow. Owns the operator chain, watermarks,
windowing, keyed state, and the shuffle between workers. Where throughput and
latency are decided.

**Control plane** — owns everything about a job that is not processing a
record: scheduling tasks onto slots, tracking membership, detecting failure,
coordinating checkpoints, restarting and rescaling.

The split matters because the two have opposite requirements. The data plane
must be fast and is allowed to be simple about failure. The control plane can be
slow and must be correct about failure.

## Testing

Correctness in a stream processor is hard to eyeball. Results depend on arrival
order, timing, and failure interleavings, so tests have to control those
explicitly rather than hope for them.

Two things the project needs early:

- **A synthetic source** with configurable throughput, key cardinality, and
  event-time skew. Reproducing "records arrive 30 seconds late, out of order" on
  demand is the only way to test time handling.
- **A correctness oracle** — an in-memory implementation that recomputes the
  expected result directly, so every job can be checked against a known answer
  rather than a hand-written expectation.

Both land with the dataflow milestone rather than at the end, so each milestone
afterwards has something to be validated against.

## References

- *Designing Data-Intensive Applications*, Kleppmann — Chapter 11. Survey of the
  problem space.
- Streaming 101 and 102, Akidau. Event time, watermarks, triggers.
- The Dataflow Model, Akidau et al., VLDB 2015. The model underneath modern
  engines.
- Naiad: A Timely Dataflow System, Murray et al., SOSP 2013. Dataflow runtime
  structure.
- MillWheel, Akidau et al., VLDB 2013. Watermark propagation in practice.
- Lightweight Asynchronous Snapshots for Distributed Dataflows, Carbone et al.
  Flink's checkpointing algorithm.
- Distributed Snapshots, Chandy and Lamport, 1985. The original.

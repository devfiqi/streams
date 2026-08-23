# streams

A distributed stream processing engine.

## About

`streams` is a from-scratch distributed stream processing system, built in Java.
It covers both halves of the stack: the execution engine that runs dataflow jobs,
and the control plane that schedules, checkpoints, and recovers them.

This is a learning project, written to understand how systems like Flink and
Kafka Streams actually work underneath. Design notes will live in `docs/` as
each piece gets built.

## References

- *Designing Data-Intensive Applications*, Martin Kleppmann — Chapter 11, Stream Processing
- [Streaming 101](https://www.oreilly.com/radar/the-world-beyond-batch-streaming-101/) and [Streaming 102](https://www.oreilly.com/radar/the-world-beyond-batch-streaming-102/), Tyler Akidau
- [The Dataflow Model](https://research.google/pubs/pub43864/), Akidau et al., VLDB 2015

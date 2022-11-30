FROM ghcr.io/graalvm/graalvm-ce:22 AS builder
COPY . /src
WORKDIR /src
RUN ./gradlew --no-daemon nativeCompile

FROM fedora:38
WORKDIR /app
COPY --from=builder /src/build/native/nativeCompile/syscan /app/syscan
CMD ["/app/syscan"]

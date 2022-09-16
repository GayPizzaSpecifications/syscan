package lgbt.mystic.syscan.tool.output

enum class OutputFormats(val id: String, val format: OutputFormat) {
  Json("json", JsonOutputFormat),
  Simple("simple", SimpleOutputFormat)
}

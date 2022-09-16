package lgbt.mystic.syscan.analysis

fun List<AnalysisStep>.filterSupportedSteps() = filter { step -> step.supported() }

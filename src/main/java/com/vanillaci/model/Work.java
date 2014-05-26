package com.vanillaci.model;

import com.google.common.collect.*;
import org.codehaus.jackson.annotate.*;
import org.jetbrains.annotations.*;

import java.util.*;

/**
 * @author Devon Moss
 */
public class Work {
	@NotNull private final String id;
	@NotNull private final Map<String, String> parameters;
	@NotNull private final List<ScriptMessage> scripts;
	@NotNull private final List<ScriptMessage> postBuildScripts;


	public Work(
		@JsonProperty("id") @NotNull String id,
		@JsonProperty("parameters") @NotNull Map<String, String> parameters,
		@JsonProperty("scripts") @NotNull List<ScriptMessage> scripts,
		@JsonProperty("postBuildScripts") @NotNull List<ScriptMessage> postBuildScripts
	) {
		this.id = id;
		this.parameters = ImmutableMap.copyOf(parameters);
		this.scripts = ImmutableList.copyOf(scripts);
		this.postBuildScripts = postBuildScripts;
	}

	@NotNull
	public String getId() {
		return id;
	}

	@NotNull
	public Map<String, String> getParameters() {
		return parameters;
	}

	@NotNull
	public List<ScriptMessage> getScripts() {
		return scripts;
	}

	@NotNull
	public List<ScriptMessage> getPostBuildScripts() {
		return postBuildScripts;
	}
}

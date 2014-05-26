package com.vanillaci.model;

import com.google.common.collect.*;
import org.jetbrains.annotations.*;

import java.util.*;

/**
 * @author Devon Moss
 */
public class Work {
	@NotNull private final Map<String, String> parameters;
	@NotNull private final List<ScriptMessage> scripts;


	public Work(@NotNull Map<String, String> parameters, @NotNull List<ScriptMessage> scripts) {
		this.parameters = ImmutableMap.copyOf(parameters);
		this.scripts = ImmutableList.copyOf(scripts);
	}

	@NotNull
	public Map<String, String> getParameters() {
		return parameters;
	}

	@NotNull
	public List<ScriptMessage> getScripts() {
		return scripts;
	}
}

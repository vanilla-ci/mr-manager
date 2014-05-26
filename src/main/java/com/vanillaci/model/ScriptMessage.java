package com.vanillaci.model;

import com.google.common.collect.*;
import org.jetbrains.annotations.*;

import java.util.*;

/**
 * @author Devon Moss
 */
public class ScriptMessage {
	@NotNull private final String name;
	@NotNull private final String version;
	@NotNull private final Map<String, String> parameters;

	public ScriptMessage(@NotNull String name, @NotNull String version, @NotNull Map<String, String> parameters) {
		this.name = name;
		this.version = version;
		this.parameters = ImmutableMap.copyOf(parameters);
	}

	@NotNull
	public String getName() {
		return name;
	}

	@NotNull
	public String getVersion() {
		return version;
	}

	@NotNull
	public Map<String, String> getParameters() {
		return parameters;
	}
}

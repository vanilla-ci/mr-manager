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
	@NotNull private final List<ScriptMessage> postScripts;


	public Work(
		@JsonProperty("id") @NotNull String id,
		@JsonProperty("parameters") @NotNull Map<String, String> parameters,
		@JsonProperty("scripts") @NotNull List<ScriptMessage> scripts,
		@JsonProperty("postScripts") @NotNull List<ScriptMessage> postScripts
	) {
		this.id = id;
		this.parameters = ImmutableMap.copyOf(parameters);
		this.scripts = ImmutableList.copyOf(scripts);
		this.postScripts = ImmutableList.copyOf(postScripts);
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
	public List<ScriptMessage> getPostScripts() {
		return postScripts;
	}

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Work work = (Work) o;

    return id.equals(work.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public String toString() {
    return "Work{" +
        "id='" + id + '\'' +
        '}';
  }
}

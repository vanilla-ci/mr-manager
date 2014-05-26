package com.vanillaci.services;

import com.google.common.collect.*;
import com.vanillaci.model.*;
import com.vanillaci.scriptbundles.*;
import org.junit.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.util.*;

/**
 * @author Devon Moss
 * @date 5/26/14.
 */
public class WorkServiceTest {
	private ScriptService scriptService;
	private Api api;

	@Before
	public void setUp() throws Exception {
		api = new Api();
		scriptService = mock(ScriptService.class);

		List<Script> scripts = ImmutableList.of(
			mock(Script.class),
			mock(Script.class),
			mock(Script.class),
			mock(Script.class),
			mock(Script.class),
			mock(Script.class)
		);

		for (Script script : scripts) {
			Process mockedProcess = mock(Process.class);
			InputStream inputStream = new InputStream() {
				@Override
				public int read() throws IOException {
					return -1;
				}
			};

			when(mockedProcess.getInputStream()).thenReturn(inputStream);
			when(mockedProcess.waitFor()).thenReturn(0);

			when(script.execute(any(File.class), any(Environment.class), any(File.class)))
				.thenReturn(mockedProcess);
		}

		when(scriptService.getScript("script1", "1.0")).thenReturn(scripts.get(0));
		when(scriptService.getScript("script2", "2.0")).thenReturn(scripts.get(1));
		when(scriptService.getScript("script3", "3.0")).thenReturn(scripts.get(2));
		when(scriptService.getScript("postScript1", "1.0")).thenReturn(scripts.get(3));
		when(scriptService.getScript("postScript2", "2.0")).thenReturn(scripts.get(4));
		when(scriptService.getScript("postScript3", "3.0")).thenReturn(scripts.get(5));
	}

	@Test
	public void testExecuteWork_allPass() {
		List<ScriptMessage> scriptMessages = ImmutableList.of(
			new ScriptMessage("script1", "1.0", Collections.emptyMap()),
			new ScriptMessage("script2", "2.0", Collections.emptyMap()),
			new ScriptMessage("script3", "3.0", Collections.emptyMap())
		);

		List<ScriptMessage> postScriptMessages = ImmutableList.of(
			new ScriptMessage("postScript1", "1.0", Collections.emptyMap()),
			new ScriptMessage("postScript2", "2.0", Collections.emptyMap()),
			new ScriptMessage("postScript3", "3.0", Collections.emptyMap())
		);

		Work work = new Work(
			"doStuff",
			Collections.<String, String>emptyMap(),
			scriptMessages,
			postScriptMessages
		);

		WorkService workService = new WorkService(scriptService, api);
		workService.execute(work);

		// TODO: assert that everything was called.
	}

	@Test
	public void testExecuteWork_firstFails_postRuns() {
		// TODO: assert that all post scripts run if any of the main scripts fail
	}
}

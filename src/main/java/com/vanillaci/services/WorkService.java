package com.vanillaci.services;

import com.vanillaci.model.*;
import com.vanillaci.scriptbundles.*;
import org.apache.logging.log4j.*;

import java.io.*;
import java.util.*;

/**
 * @author Devon Moss
 */
public class WorkService {
	private static final Logger log = LogManager.getLogger();
	private final ScriptService scriptService;
	private final Api api;

	public WorkService(ScriptService scriptService, Api api) {
		this.scriptService = scriptService;
		this.api = api;
	}

	/**
	 * Executes all the steps synchronously provided in the work object.
	 */
	public void execute(Work work)  {
		File workingDirectoryForWork = getWorkingDirectoryForWork(work);

		try {
			for (ScriptMessage scriptMessage : work.getScripts()) {
				int exitCode = executeScriptMessage(work, workingDirectoryForWork, scriptMessage);
				if(exitCode != 0) {
					log.info("Step failed: %s %s", scriptMessage.getName(), scriptMessage.getVersion());
					// TODO: Status update message here
					break;
				} else {
					log.trace("Step succeeded: %s %s", scriptMessage.getName(), scriptMessage.getVersion());
					// TODO: Status update message here
				}
			}
		} catch (InterruptedException | IOException e) {
			log.error("Exception while running main scripts", e);
			// TODO: Status update message here
		} finally {
			for (ScriptMessage postScriptMessage : work.getPostScripts()) {
				try {
					int exitCode = executeScriptMessage(work, workingDirectoryForWork, postScriptMessage);
					if(exitCode != 0) {
						log.info("Post step failed: %s %s", postScriptMessage.getName(), postScriptMessage.getVersion());
						// TODO: Status update message here
					} else {
						log.trace("Post step succeeded: %s %s", postScriptMessage.getName(), postScriptMessage.getVersion());
						// TODO: Status update message here
					}
				} catch (InterruptedException | IOException e) {
					log.error("Exception while running post scripts", e);
					// TODO: Status update message here
				}
			}
		}
	}

	private int executeScriptMessage(Work work, File workingDirectoryForWork, ScriptMessage scriptMessage) throws IOException, InterruptedException {
		Environment environment = new Environment();
		environment.addEnvironmentVariables(work.getParameters());
		environment.addEnvironmentVariables(scriptMessage.getParameters());

		Script script = scriptService.getScript(scriptMessage.getName(), scriptMessage.getVersion());
		Process process = null;

		try {
			process = script.execute(workingDirectoryForWork, environment, null);// TODO: do something with the stderr

			InputStream processStdOut = process.getInputStream();
			try(Scanner scanner = new Scanner(processStdOut)) {
				while(scanner.hasNextLine()) {
					String line = scanner.nextLine();
					api.processRawCommand(work, line);
				}
			}

			return process.waitFor();
		} catch (InterruptedException | IOException e) {
			if(process != null) {
				process.destroy();
			}
			throw e;
		}
	}

	private File getWorkingDirectoryForWork(Work work) {
		//TODO: Make less crappy :(
		String vanillaciHomeEnvVar = System.getenv("VANILLACI_HOME");
		if(vanillaciHomeEnvVar == null) {
			vanillaciHomeEnvVar = "./vanillaCiHome";
		}
		File homeDir = new File(vanillaciHomeEnvVar);
		File workDir = new File(homeDir, "workingDirectories");
		if(!workDir.exists() && !workDir.mkdirs()) {
			throw new RuntimeException("Could not make working directory: " + workDir.getAbsolutePath());
		}

		String id = work.getId(); // TODO: slugify!!
		return new File(workDir, id);
	}
}

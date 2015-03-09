/**
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at

 *  http://www.apache.org/licenses/LICENSE-2.0

 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.stratos.cli.commands;

import org.apache.commons.cli.*;
import org.apache.stratos.cli.Command;
import org.apache.stratos.cli.RestCommandLineService;
import org.apache.stratos.cli.StratosCommandContext;
import org.apache.stratos.cli.exception.CommandException;
import org.apache.stratos.cli.utils.CliConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Deploy application command.
 */
public class DeployApplicationCommand implements Command<StratosCommandContext> {

    private static final Logger logger = LoggerFactory.getLogger(DeployApplicationCommand.class);

    private Options options;

    public DeployApplicationCommand(){
        options = constructOptions();
    }

    private Options constructOptions() {
        final Options options = new Options();

        Option applicationOption = new Option(CliConstants.APPLICATION_ID_OPTION, CliConstants.APPLICATION_ID_LONG_OPTION, true,
                "Application option");
        applicationOption.setArgName("applicationId");
        options.addOption(applicationOption);

        Option applicationPolicyOption = new Option(CliConstants.APPLICATION_POLICY_ID_OPTION, CliConstants.APPLICATION_POLICY_ID_LONG_OPTION, true,
                "Application policy");
        applicationPolicyOption.setArgName("applicationPolicyId");
        options.addOption(applicationPolicyOption);

        return options;
    }

    @Override
    public String getName() {
        return "deploy-application";
    }

    @Override
    public String getDescription() {
        return "deploy application";
    }

    @Override
    public String getArgumentSyntax() {
        return "[application-id]";
    }

    @Override
    public Options getOptions() {
        return options;
    }

    @Override
    public int execute(StratosCommandContext context, String[] args) throws CommandException {
        if (logger.isDebugEnabled()) {
            logger.debug("Executing {} command...", getName());
        }

        if (args != null && args.length > 0) {
            String applicationId= null;
            String applicationPolicyId= null;

            final CommandLineParser parser = new GnuParser();
            CommandLine commandLine;

            try {
                commandLine = parser.parse(options, args);

                if (logger.isDebugEnabled()) {
                    logger.debug("Deploy application");
                }

                if (commandLine.hasOption(CliConstants.APPLICATION_ID_OPTION)) {
                    if (logger.isTraceEnabled()) {
                        logger.trace("Application Id option is passed");
                    }
                    applicationId = commandLine.getOptionValue(CliConstants.APPLICATION_ID_OPTION);
                }
                if (commandLine.hasOption(CliConstants.APPLICATION_POLICY_ID_OPTION)) {
                    if (logger.isTraceEnabled()) {
                        logger.trace("Application policy Id option is passed");
                    }
                    applicationPolicyId = commandLine.getOptionValue(CliConstants.APPLICATION_POLICY_ID_OPTION);
                }

                if (applicationId == null || applicationPolicyId == null) {
                    System.out.println("usage: " + getName() + " [-a <application id>] [-ap <application policy id>]");
                    return CliConstants.COMMAND_FAILED;
                }

                RestCommandLineService.getInstance().deployApplication(applicationId,applicationPolicyId);
                return CliConstants.COMMAND_SUCCESSFULL;

            } catch (ParseException e) {
                if (logger.isErrorEnabled()) {
                    logger.error("Error parsing arguments", e);
                }
                System.out.println(e.getMessage());
                return CliConstants.COMMAND_FAILED;
            }

        } else {
            context.getStratosApplication().printUsage(getName());
            return CliConstants.COMMAND_FAILED;
        }
    }
}

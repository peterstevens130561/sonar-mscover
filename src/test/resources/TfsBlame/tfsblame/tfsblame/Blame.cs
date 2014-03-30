// =============================================================================
// =
// =   FILE:		Blame.cs
// =
// =============================================================================
// =                                                                        
// = COPYRIGHT: Title to, ownership of and copyright of this software is
// = vested in Copyright 2003-2013  Baker Hughes Reservoir Software BV.
// = is a wholly-owned subsidiary of Baker Hughes Incorporated.
// = All rights reserved.
// =
// = Neither the whole nor any part of this software may be
// = reproduced, copied, stored in any retrieval system or
// = transmitted in any form or by any means without prior
// = written consent of the copyright owner.
// =
// = This software and the information and data it contains is
// = confidential. Neither the whole nor any part of the
// = software and the data and information it contains may be
// = disclosed to any third party without the prior written
// = consent of Copyright 2003-2013 Baker Hughes Reservoir Software BV, a
// = wholly-owned subsidiary of Baker Hughes Incorporated 
// =                                                                          
// =============================================================================
using System;
using System.Globalization;
using System.Text;
using System.Text.RegularExpressions;
using System.Diagnostics ;

namespace BHI.JewelSuite.Tools.TfsBlame
{

    public class Blame
    {
        private ICommits commits = new Commits();

        public Blame()
        {
            
        }

        public Blame(ICommits commits)
        {
            this.commits = commits;
        }

        public String RunAnnotate(String fileName)
        {
            var standardOutput = new StringBuilder();
            var standardError = new StringBuilder();
            using (var myProcess = new Process())
            {
                myProcess.StartInfo.UseShellExecute = false;
                myProcess.StartInfo.FileName = "tfpt.exe";
                myProcess.StartInfo.Arguments = "annotate /noprompt " + fileName;
                myProcess.StartInfo.CreateNoWindow = true;
                myProcess.StartInfo.RedirectStandardError = true;
                myProcess.StartInfo.RedirectStandardOutput = true;
                myProcess.StartInfo.WorkingDirectory = Environment.CurrentDirectory;
                myProcess.OutputDataReceived += (sender, args) => standardOutput.AppendLine(DecorateLine(args.Data));
                myProcess.ErrorDataReceived += (sender, args) => standardError.AppendLine(args.Data);
                myProcess.Start();
                myProcess.BeginOutputReadLine();
                myProcess.BeginErrorReadLine();
                myProcess.WaitForExit();
                int exitCode=myProcess.ExitCode;
                if (exitCode > 0)
                {
                    throw new TfsBlameException(standardError.ToString());
                }
            }
            return standardOutput.ToString();

        }


        public String DecorateLine(String line)
        {
            const string changeSetpattern = "^[0-9]+";
            const string beforeLinePattern = changeSetpattern + "\\s+";
            if (line == null)
            {
                return String.Empty;
            }
            MatchCollection matches = Regex.Matches(line, changeSetpattern);
            if (matches.Count == 0)
            {
                return String.Empty;
            }

            var changesetKey = matches[0].Value;
            int changesetId = Convert.ToInt32(changesetKey,CultureInfo.InvariantCulture);
            var formattedCommit = commits.GetFormattedCommit(changesetId);
            var lineWithoutChangesetId = Regex.Replace(line, beforeLinePattern, "");
            return formattedCommit  + " " + lineWithoutChangesetId;
        }
    }

}

// =============================================================================
// =
// = FILE: Program.cs
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
using System.Reflection;

namespace BHI.JewelSuite.Tools.TfsBlame
{
    class Program
    {
        static void Main(string[] args)
        {
            try
            {
                TryBlame(args);
            } catch (Exception e)
            {
                Console.Write(e.Message);
            }

        }

        static private void TryBlame(string[] args)
        {
            ArgumentParser parser = new ArgumentParser();
            parser.Parse(args);
            if (parser.Version)
            {
                Console.WriteLine(Assembly.GetExecutingAssembly().GetName().Version);
                return;
            }
            String file = parser.File;
            String url = parser.Url;
            if (file == null || url == null)
            {
                Usage();
                return;
            }
            Commits commits = new Commits();
            commits.Connect(url);
            Blame blame = new Blame(commits);

            String result=blame.RunAnnotate(file);
            Console.WriteLine(result);
        }

        private static void Usage()
        {
            Console.WriteLine("Usage: tfsblame <File>");
        }
    }
}

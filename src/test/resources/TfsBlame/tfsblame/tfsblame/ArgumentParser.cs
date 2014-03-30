// =============================================================================
// =
// = FILE: ArgumentParser.cs
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

namespace BHI.JewelSuite.Tools.TfsBlame
{
    public class ArgumentParser
    {
        public string File { get; private set; }
        public Boolean Version { get; private set; }
        public string Url { get; private set;  }

        public void Parse(string[] arguments)
        {
            foreach (var argument in arguments)
            {
                if (!argument.Equals("/noprompt"))
                {
                    File = argument;
                }
                if (argument.Equals("/version"))
                {
                    Version = true;
                }
                if (argument.StartsWith("/url:"))
                {
                    if (argument.Length > 5)
                    {
                        Url = argument.Substring(5);
                        if (!Url.StartsWith("http"))
                        {
                            throw new TfsBlameException("Invalid Url" + Url);
                        }
                    }
                }

            }
        }


    }
}

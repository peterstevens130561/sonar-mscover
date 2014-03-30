using System ;
using JetBrains.Annotations;
namespace BHI.JewelSuite.Tools.TfsBlame
{
    internal interface IServerUriFinder
    {
        Uri FindUri(string directory);
    }

    class ServerUriFinder : IServerUriFinder
    {
        /// <summary>
        /// try to get the uri from the environment
        /// then return the value
        /// </summary>
        /// <param name="spec"></param>
        /// <returns></returns>
        [NotNull]
        public Uri FindUri(String spec)
        {
            Uri workspaceUri;
            try
            {
                workspaceUri = new Uri(spec);
            }
            catch (UriFormatException e)
            {
                throw new TfsBlameException("Invalid uri format of '" + spec + "' " + e.Message);
            }
            return workspaceUri;
        }
    }
}

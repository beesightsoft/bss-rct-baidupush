using ReactNative.Bridge;
using System;
using System.Collections.Generic;
using Windows.ApplicationModel.Core;
using Windows.UI.Core;

namespace Bss.Baidupush.RNBssBaidupush
{
    /// <summary>
    /// A module that allows JS to share data.
    /// </summary>
    class RNBssBaidupushModule : NativeModuleBase
    {
        /// <summary>
        /// Instantiates the <see cref="RNBssBaidupushModule"/>.
        /// </summary>
        internal RNBssBaidupushModule()
        {

        }

        /// <summary>
        /// The name of the native module.
        /// </summary>
        public override string Name
        {
            get
            {
                return "RNBssBaidupush";
            }
        }
    }
}

package org.lamport.tla.toolbox.tool.tlc.launch.ui;

import java.util.HashMap;
import java.util.Vector;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.actions.CompoundContributionItem;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.lamport.tla.toolbox.tool.ToolboxHandle;
import org.lamport.tla.toolbox.tool.tlc.TLCActivator;
import org.lamport.tla.toolbox.tool.tlc.handlers.OpenModelHandler;
import org.lamport.tla.toolbox.tool.tlc.launch.TLCModelLaunchDelegate;
import org.lamport.tla.toolbox.util.UIHelper;

/**
 * Contributes a list of models
 * @author Simon Zambrovski
 * @version $Id$
 */
public class ModelContributionItem extends CompoundContributionItem
{
    private ImageDescriptor modelIcon = TLCActivator.getImageDescriptor("icons/full/choice_sc_obj.gif");

    /**
     * @see org.eclipse.ui.actions.CompoundContributionItem#getContributionItems()
     */
    protected IContributionItem[] getContributionItems()
    {

        ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
        ILaunchConfigurationType launchConfigurationType = launchManager
                .getLaunchConfigurationType(TLCModelLaunchDelegate.LAUNCH_ID);

        Vector modelContributions = new Vector();

        IProject specProject = ToolboxHandle.getCurrentSpec().getProject();
        try
        {
            ILaunchConfiguration[] launchConfigurations = launchManager
                    .getLaunchConfigurations(launchConfigurationType);
            for (int i = 0; i < launchConfigurations.length; i++)
            {
                String modelName = launchConfigurations[i].getName();

                // skip launches from other specs
                if (!specProject.equals(launchConfigurations[i].getFile().getProject()))
                {
                    continue;
                }

                HashMap parameters = new HashMap();

                // fill the model name for the handler
                parameters.put(OpenModelHandler.PARAM_MODEL_NAME, modelName);

                // create the contribution item
                CommandContributionItemParameter param = new CommandContributionItemParameter(UIHelper
                        .getActiveWindow(), "toolbox.command.model.open." + modelName, OpenModelHandler.COMMAND_ID,
                        parameters, modelIcon, null, null, modelName, null, "Opens " + modelName,
                        CommandContributionItem.STYLE_PUSH, null, true);

                // add contribution item to the list
                modelContributions.add(new CommandContributionItem(param));
            }

        } catch (CoreException e)
        {

        }
        return (IContributionItem[]) modelContributions.toArray(new IContributionItem[modelContributions.size()]);
    }

}

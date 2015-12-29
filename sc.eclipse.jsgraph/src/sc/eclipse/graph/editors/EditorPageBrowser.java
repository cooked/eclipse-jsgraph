package sc.eclipse.graph.editors;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.part.EditorPart;
import org.osgi.framework.Bundle;

import sc.eclipse.graph.http.HttpUtils;

public class EditorPageBrowser extends EditorPart { //extends FormPage {

	//public static final String ID = "sc.eclipse.graph.editors.MultiPageEditor.browser"; //$NON-NLS-1$

	private Browser browser;
	public boolean initialized;

	@Override
	public void createPartControl(Composite parent) {

		HttpUtils.getDefaultServer();

		try {
			GridLayout layout = new GridLayout();
			layout.marginHeight = 0;
			layout.marginWidth = 0;
			layout.horizontalSpacing = 0;
			layout.verticalSpacing = 0;

			browser = createSWTBrowser(parent);
			browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			
		} catch (SWTError e) {
			MessageBox messageBox = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
			messageBox.setMessage("Browser cannot be initialized.");
			messageBox.setText("Exit");
			messageBox.open();
		}

		browser.setUrl("http://localhost:8888/" + HttpUtils.page);

		browser.addControlListener(new ControlListener() {
			public void controlResized(ControlEvent e) {
			}
			public void controlMoved(ControlEvent e) {
			}
		});

		browser.addProgressListener(new ProgressListener() {


			@Override
			public void completed(ProgressEvent event) {
				initialized = true;
			}
			@Override
			public void changed(ProgressEvent event) {
			}
		});

		browser.addLocationListener(new LocationListener(){
			@Override
			public void changing(LocationEvent event) {
			}
			@Override
			public void changed(LocationEvent event) {
			}

		});
	}


	private Browser createSWTBrowser(Composite parent) {

		try {
			if (System.getProperty(HttpUtils.XULRUNNER_ENV) == null) {
				Bundle bundle = null;
				if (Platform.getOS().matches(Platform.OS_WIN32))
					bundle = Platform.getBundle(HttpUtils.XULRUNNER_WIN32_PLUGIN);
				else if (Platform.getOS().matches(Platform.OS_MACOSX))
					bundle = Platform.getBundle(HttpUtils.XULRUNNER_MAC_PLUGIN);

				if (bundle != null) {
					URL xulrunner = bundle.getEntry(HttpUtils.XULRUNNER_PATH);
					if (xulrunner != null) {
						try {
							xulrunner = FileLocator.toFileURL(xulrunner);
							if (xulrunner != null) {
								File xulrunnerFolder = new File(xulrunner.getFile());
								System.setProperty(HttpUtils.XULRUNNER_ENV, xulrunnerFolder.getAbsolutePath());
							}
						} catch (IOException e) {
						}
					}
				}
			}
		} catch (Exception e) {
		}

		return new Browser(parent, HttpUtils.getEngine());

	}


	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) {

		setSite(site);
		setInput(input);
		setPartName("plot");

	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}

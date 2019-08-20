
package com.rapidclipse.demo.hashing.ui;

import java.beans.Beans;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Provider;
import java.security.Security;

import org.apache.commons.lang3.text.StrBuilder;

import com.rapidclipse.framework.security.util.PasswordHasher;
import com.rapidclipse.framework.server.data.converter.HashConverter.Implementation.Hex;
import com.rapidclipse.framework.server.security.authentication.AccessibleView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.FormItem;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinService;


@Route("common")
@AccessibleView
public class CommonLayout extends SplitLayout
{
	private final String mypassword = "mypassword123$§";
	
	/**
	 *
	 */
	public CommonLayout()
	{
		super();
		
		this.initUI();
		
		this.lblDefaultCharset.setText(Charset.defaultCharset().toString());
		this.lblPassword.setText(this.mypassword);
	}
	
	private void addToTextarea(final String text, final boolean reset)
	{
		this.textArea.setReadOnly(false);
		if(reset)
		{
			this.textArea.setValue(text);
		}
		else
		{
			this.textArea.setValue(this.textArea.getValue() + "\n" + text);
		}
		this.textArea.setReadOnly(true);
	}
	
	/**
	 * Event handler delegate method for the {@link Button} {@link #cmdShowProviders}.
	 *
	 * @see ComponentEventListener#onComponentEvent(ComponentEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdShowProviders_onClick(final ClickEvent<Button> event)
	{
		final StrBuilder sb = new StrBuilder();
		
		// Get all available provider
		for(final Provider provider : Security.getProviders())
		{
			sb.appendln("---");
			
			// print provider info
			sb.appendln("Provider: " + provider.getInfo());
			
			sb.appendln("Available services:");
			
			// get provider's services and print it's algorithm
			provider.getServices().forEach(s -> {
				
				sb.appendln(" -> " + s.getAlgorithm());
				
			});
		}
		
		this.addToTextarea(sb.toString(), true);
	}
	
	/**
	 * Event handler delegate method for the {@link Button} {@link #cmdRCHashMD5}.
	 *
	 * @see ComponentEventListener#onComponentEvent(ComponentEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdRCHashMD5_onClick(final ClickEvent<Button> event)
	{
		final StrBuilder sb = new StrBuilder();
		
		final byte[] hashedPassword = PasswordHasher.Md5().hashPassword(this.mypassword.getBytes());
		
		sb.appendln("RC HashStrategy with MD5");
		sb.appendln(new String(hashedPassword));
		sb.appendln(Hex.encodeToString(hashedPassword));
		
		this.addToTextarea(sb.toString(), true);
	}
	
	/**
	 * Event handler delegate method for the {@link Button} {@link #cmdRCHashPBKDF2}.
	 *
	 * @see ComponentEventListener#onComponentEvent(ComponentEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdRCHashPBKDF2_onClick(final ClickEvent<Button> event)
	{
		final StrBuilder sb = new StrBuilder();
		
		final byte[] hashedPassword = new PasswordHasher.Pbkdf2withHmacSha1().hashPassword(this.mypassword.getBytes());
		
		sb.appendln("RC HashStrategy with PBKDF2WithHmacSHA1");
		sb.appendln(new String(hashedPassword));
		sb.appendln(Hex.encodeToString(hashedPassword));
		
		this.addToTextarea(sb.toString(), true);
	}
	
	/**
	 * Event handler delegate method for the {@link Button} {@link #cmdRCUTF8}.
	 *
	 * @see ComponentEventListener#onComponentEvent(ComponentEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdRCUTF8_onClick(final ClickEvent<Button> event)
	{
		final StrBuilder sb = new StrBuilder();
		
		final byte[] hashedPassword =
			PasswordHasher.Sha2().hashPassword(this.mypassword.getBytes(StandardCharsets.UTF_8));
		
		sb.appendln("Default: RC HashStrategy and UTF-8");
		sb.appendln(new String(hashedPassword));
		sb.appendln(Hex.encodeToString(hashedPassword));
		
		this.addToTextarea(sb.toString(), true);
	}
	
	/**
	 * Event handler delegate method for the {@link Button} {@link #cmdRCISO}.
	 *
	 * @see ComponentEventListener#onComponentEvent(ComponentEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdRCISO_onClick(final ClickEvent<Button> event)
	{
		final StrBuilder sb = new StrBuilder();
		
		final byte[] hashedPassword =
			PasswordHasher.Sha2().hashPassword(this.mypassword.getBytes(Charset.forName("ISO-8859-1")));
		
		sb.appendln("RC HashStrategy and ISO-8859-1");
		sb.appendln(new String(hashedPassword));
		sb.appendln(Hex.encodeToString(hashedPassword));
		
		this.addToTextarea(sb.toString(), true);
	}
	
	/**
	 * Event handler delegate method for the {@link Button} {@link #cmdRCUTF16}.
	 *
	 * @see ComponentEventListener#onComponentEvent(ComponentEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void cmdRCUTF16_onClick(final ClickEvent<Button> event)
	{
		final StrBuilder sb = new StrBuilder();
		
		final byte[] hashedPassword =
			PasswordHasher.Sha2().hashPassword(this.mypassword.getBytes(StandardCharsets.UTF_16));
		
		sb.appendln("RC HashStrategy and UTF-16");
		sb.appendln(new String(hashedPassword));
		sb.appendln(Hex.encodeToString(hashedPassword));
		
		this.addToTextarea(sb.toString(), true);
	}
	
	/* WARNING: Do NOT edit!<br>The content of this method is always regenerated by the UI designer. */
	// <generated-code name="initUI">
	private void initUI()
	{
		this.verticalLayout    = new VerticalLayout();
		this.routerLink        = new RouterLink();
		this.hr2               = new Hr();
		this.formLayout        = new FormLayout();
		this.formItem          = new FormItem();
		this.label2            = new Label();
		this.lblDefaultCharset = new Label();
		this.formItem2         = new FormItem();
		this.label3            = new Label();
		this.lblPassword       = new Label();
		this.hr                = new Hr();
		this.label             = new Label();
		this.cmdShowProviders  = new Button();
		this.hr3               = new Hr();
		this.label4            = new Label();
		this.cmdRCHashMD5      = new Button();
		this.cmdRCHashPBKDF2   = new Button();
		this.hr4               = new Hr();
		this.label5            = new Label();
		this.cmdRCUTF8         = new Button();
		this.cmdRCISO          = new Button();
		this.cmdRCUTF16        = new Button();
		this.verticalLayout2   = new VerticalLayout();
		this.textArea          = new TextArea();

		this.routerLink.setText("Back");
		if(!Beans.isDesignTime())
		{
			this.routerLink.setRoute(VaadinService.getCurrent().getRouter(), MainLayout.class);
		}
		this.label2.setText("Default Charset:");
		this.lblDefaultCharset.setText("Label");
		this.label3.setText("Password:");
		this.lblPassword.setText("Label");
		this.label.setText("List all available provider for the Java Security API and it's services:");
		this.cmdShowProviders.setText("Show provider and services");
		this.label4.setText("Using default charset (utf-8) with different hashing algorithms");
		this.cmdRCHashMD5.setText("Rapidclipse Hashstrategy & MD5");
		this.cmdRCHashPBKDF2.setText("Rapidclipse Hashstrategy & PBKDF2WithHmacSHA1");
		this.label5.setText("RapidClipse Hashstrategy, SHA256 with different charsets");
		this.cmdRCUTF8.setText("Default: Rapidclipse Hashstrategy &UTF-8");
		this.cmdRCISO.setText("Rapidclipse Hashstrategy & ISO-8859-1");
		this.cmdRCUTF16.setText("Rapidclipse Hashstrategy & UTF-16");

		this.label2.setSizeUndefined();
		this.label2.getElement().setAttribute("slot", "label");
		this.lblDefaultCharset.setWidthFull();
		this.lblDefaultCharset.setHeight(null);
		this.formItem.add(this.label2, this.lblDefaultCharset);
		this.label3.setSizeUndefined();
		this.label3.getElement().setAttribute("slot", "label");
		this.lblPassword.setWidthFull();
		this.lblPassword.setHeight(null);
		this.formItem2.add(this.label3, this.lblPassword);
		this.formLayout.add(this.formItem, this.formItem2);
		this.hr2.setWidthFull();
		this.hr2.setHeight(null);
		this.formLayout.setWidthFull();
		this.formLayout.setHeight(null);
		this.hr.setWidthFull();
		this.hr.setHeight(null);
		this.label.setSizeUndefined();
		this.cmdShowProviders.setSizeUndefined();
		this.hr3.setWidthFull();
		this.hr3.setHeight(null);
		this.label4.setSizeUndefined();
		this.cmdRCHashMD5.setSizeUndefined();
		this.cmdRCHashPBKDF2.setSizeUndefined();
		this.hr4.setWidthFull();
		this.hr4.setHeight(null);
		this.label5.setSizeUndefined();
		this.cmdRCUTF8.setSizeUndefined();
		this.cmdRCISO.setSizeUndefined();
		this.cmdRCUTF16.setSizeUndefined();
		this.verticalLayout.add(this.routerLink, this.hr2, this.formLayout, this.hr, this.label, this.cmdShowProviders,
			this.hr3, this.label4, this.cmdRCHashMD5, this.cmdRCHashPBKDF2, this.hr4, this.label5, this.cmdRCUTF8,
			this.cmdRCISO, this.cmdRCUTF16);
		this.textArea.setSizeFull();
		this.verticalLayout2.add(this.textArea);
		this.addToPrimary(this.verticalLayout);
		this.addToSecondary(this.verticalLayout2);
		this.setSplitterPosition(50.0);
		this.setSizeFull();

		this.cmdShowProviders.addClickListener(this::cmdShowProviders_onClick);
		this.cmdRCHashMD5.addClickListener(this::cmdRCHashMD5_onClick);
		this.cmdRCHashPBKDF2.addClickListener(this::cmdRCHashPBKDF2_onClick);
		this.cmdRCUTF8.addClickListener(this::cmdRCUTF8_onClick);
		this.cmdRCISO.addClickListener(this::cmdRCISO_onClick);
		this.cmdRCUTF16.addClickListener(this::cmdRCUTF16_onClick);
	} // </generated-code>
	
	// <generated-code name="variables">
	private FormLayout     formLayout;
	private Button         cmdShowProviders, cmdRCHashMD5, cmdRCHashPBKDF2, cmdRCUTF8, cmdRCISO, cmdRCUTF16;
	private TextArea       textArea;
	private VerticalLayout verticalLayout, verticalLayout2;
	private RouterLink     routerLink;
	private Label          label2, lblDefaultCharset, label3, lblPassword, label, label4, label5;
	private Hr             hr2, hr, hr3, hr4;
	private FormItem       formItem, formItem2;
	// </generated-code>
	
}

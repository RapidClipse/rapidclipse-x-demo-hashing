
package com.rapidclipse.demo.hashing.ui;

import java.beans.Beans;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.text.StrBuilder;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.util.ByteSource;

import com.rapidclipse.demo.hashing.MyAuthenticationProvider;
import com.rapidclipse.framework.security.util.PasswordHasher;
import com.rapidclipse.framework.server.data.converter.HashConverter.Implementation.Hex;
import com.rapidclipse.framework.server.resources.CaptionUtils;
import com.rapidclipse.framework.server.security.authentication.AccessibleView;
import com.rapidclipse.framework.server.security.authentication.jpa.JPAAuthenticator;
import com.rapidclipse.framework.server.ui.ItemLabelGeneratorFactory;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.FormItem;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinService;


@Route("hash")
@AccessibleView
public class HashDemoLayout extends SplitLayout
{
	private final String       mypassword = "mypassword123$§";
	private final List<String> choices    = Arrays.asList("shortened", "detailed");
	
	/**
	 *
	 */
	public HashDemoLayout()
	{
		super();
		
		this.initUI();
		
		this.lblDefaultCharset.setText(Charset.defaultCharset().toString());
		this.lblPassword.setText(this.mypassword);
		
		this.optionRCICondense.setDataProvider(DataProvider.ofCollection(this.choices));
		this.optionRCICondense.setValue(this.choices.get(0));
		
		this.optionRCIICondense.setDataProvider(DataProvider.ofCollection(this.choices));
		this.optionRCIICondense.setValue(this.choices.get(0));
		
		this.optionMDCondense.setDataProvider(DataProvider.ofCollection(this.choices));
		this.optionMDCondense.setValue(this.choices.get(0));
	}
	
	/**
	 * Event handler delegate method for the {@link Button} {@link #buttonRCIHash}.
	 *
	 * @see ComponentEventListener#onComponentEvent(ComponentEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void buttonRCIHash_onClick(final ClickEvent<Button> event)
	{
		final StrBuilder sb = new StrBuilder();
		
		if(this.optionRCICondense.getValue().equals("detailed"))
		{
			
			// Get the password as bytearray
			final byte[] passwordAsByteArray = this.mypassword.getBytes();
			
			// Get an instance of the provider
			final MyAuthenticationProvider myAuthProvider = MyAuthenticationProvider.getInstance();
			
			// Get the provider's authenticator
			final JPAAuthenticator authenticator = (JPAAuthenticator)myAuthProvider.provideAuthenticator();
			
			// Get the HashStrategy
			final PasswordHasher hashStrategy = authenticator.getPasswordHasher();
			
			// Now hash the password
			final byte[] hashedPassword = hashStrategy.hashPassword(passwordAsByteArray);
			
			sb.appendln(" ------------------------------------- ");
			sb.appendln("Rapidclipse - with HashStrategy from AuthenticationProvider");
			sb.appendln("[detailed]");
			sb.appendln(" ------------------------------------- ");
			sb.appendln("The password hash with \"new String(hashedPassword)\":");
			sb.appendln("\t" + new String(hashedPassword));
			sb.appendln(" ------------------------------------- ");
			sb.appendln("The password hash as hex representation:");
			sb.appendln("\t" + Hex.encodeToString(hashedPassword));
		}
		else
		{
			
			// All-in-one with Rapidclipse's Hashstrategy from the provider
			final byte[] hashedPassword =
				((JPAAuthenticator)MyAuthenticationProvider.getInstance().provideAuthenticator())
					.getPasswordHasher()
					.hashPassword(this.mypassword.getBytes());
			
			sb.appendln(" ------------------------------------- ");
			sb.appendln("Rapidclipse - with Authenticator from AuthenticationProvider");
			sb.appendln("[shortened]");
			sb.appendln(" ------------------------------------- ");
			sb.appendln("The password hash with \"new String(hashedPassword)\":");
			sb.appendln("\t" + new String(hashedPassword));
			sb.appendln(" ------------------------------------- ");
			sb.appendln("The password hash as hex representation:");
			sb.appendln("\t" + Hex.encodeToString(hashedPassword));
		}
		
		this.addToTextarea(sb.toString(), true);
		System.out.println(sb.toString());
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
	 * Event handler delegate method for the {@link Button} {@link #buttonRCII}.
	 *
	 * @see ComponentEventListener#onComponentEvent(ComponentEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void buttonRCII_onClick(final ClickEvent<Button> event)
	{
		final StrBuilder sb = new StrBuilder();
		
		if(this.optionRCIICondense.getValue().equals("detailed"))
		{
			
			// Get the password as bytearray
			final byte[] passwordAsByteArray = this.mypassword.getBytes();
			
			// Get a new SHA-256 HashStrategy object
			final PasswordHasher hashStrategy = PasswordHasher.Sha2();
			
			// Now hash the password
			final byte[] hashedPassword = hashStrategy.hashPassword(passwordAsByteArray);
			
			sb.appendln(" ------------------------------------- ");
			sb.appendln("Rapidclipse - with new HashStrategy object");
			sb.appendln("[detailed]");
			sb.appendln(" ------------------------------------- ");
			sb.appendln("The password hash with \"new String(hashedPassword)\":");
			sb.appendln("\t" + new String(hashedPassword));
			sb.appendln(" ------------------------------------- ");
			sb.appendln("The password hash as hex representation:");
			sb.appendln("\t" + Hex.encodeToString(hashedPassword));
		}
		else
		{
			
			// All-in-one with new Hashstrategy object
			final byte[] hashedPassword = PasswordHasher.Sha2().hashPassword(this.mypassword.getBytes());
			
			sb.appendln(" ------------------------------------- ");
			sb.appendln("Rapidclipse -  with new HashStrategy object");
			sb.appendln("[shortened]");
			sb.appendln(" ------------------------------------- ");
			sb.appendln("The password hash with \"new String(hashedPassword)\":");
			sb.appendln("\t" + new String(hashedPassword));
			sb.appendln(" ------------------------------------- ");
			sb.appendln("The password hash as hex representation:");
			sb.appendln("\t" + Hex.encodeToString(hashedPassword));
		}
		
		this.addToTextarea(sb.toString(), true);
		System.out.println(sb.toString());
	}
	
	/**
	 * Event handler delegate method for the {@link Button} {@link #buttonMD}.
	 *
	 * @see ComponentEventListener#onComponentEvent(ComponentEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void buttonMD_onClick(final ClickEvent<Button> event)
	{
		try
		{
			
			final StrBuilder sb = new StrBuilder();
			
			if(this.optionMDCondense.getValue().equals("detailed"))
			{
				
				// Get the password as bytearray
				final byte[] passwordAsByteArray = this.mypassword.getBytes();
				
				// Get the MessageDigest instance for SHA-256
				final MessageDigest mySha256Instance = MessageDigest.getInstance("SHA-256");
				
				// Update the instance with the current password
				mySha256Instance.update(passwordAsByteArray);
				
				// Get the hash for the password (here: called digest)
				final byte[] hashedPassword = mySha256Instance.digest();
				
				sb.appendln(" ------------------------------------- ");
				sb.appendln("Hashing a password with the MessageDigest class");
				sb.appendln("[detailed]");
				sb.appendln(" ------------------------------------- ");
				sb.appendln("The password hash with \"new String(hashedPassword)\":");
				sb.appendln("\t" + new String(hashedPassword));
				sb.appendln(" ------------------------------------- ");
				sb.appendln("The password hash as hex representation:");
				sb.appendln("\t" + Hex.encodeToString(hashedPassword));
			}
			else
			{
				
				// All-in-one with a SHA-256 MessageDigest instance
				final byte[] hashedPassword = MessageDigest.getInstance("SHA-256").digest(this.mypassword.getBytes());
				
				sb.appendln(" ------------------------------------- ");
				sb.appendln("Hashing a password with the MessageDigest class");
				sb.appendln("[shortened]");
				sb.appendln(" ------------------------------------- ");
				sb.appendln("The password hash with \"new String(hashedPassword)\":");
				sb.appendln("\t" + new String(hashedPassword));
				sb.appendln(" ------------------------------------- ");
				sb.appendln("The password hash as hex representation:");
				sb.appendln("\t" + Hex.encodeToString(hashedPassword));
			}
			
			this.addToTextarea(sb.toString(), true);
			System.out.println(sb.toString());
			
		}
		catch(final NoSuchAlgorithmException e)
		{
			Notification.show("A error has occurred: No such algorithm");
			e.printStackTrace();
		}
	}
	
	/**
	 * Event handler delegate method for the {@link Button} {@link #buttonShiro}.
	 *
	 * @see ComponentEventListener#onComponentEvent(ComponentEvent)
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 */
	private void buttonShiro_onClick(final ClickEvent<Button> event)
	{
		final StrBuilder sb = new StrBuilder();
		
		if(!this.chkSalt.getValue())
		{
			/*
			 * Checkbox not checked -> hash without salt
			 */
			
			// Get a Shiro Sha256Hash object
			final Sha256Hash sha256Hash = new Sha256Hash(this.mypassword);
			
			// Get this hash as a byte array
			final byte[] passwordAsByteArray = sha256Hash.getBytes();
			
			// Get a hex representation with "toHex()"-method of the Shiro hash object
			final String hexEncoded = sha256Hash.toHex();
			
			sb.appendln(" ------------------------------------- ");
			sb.appendln("Hashing a password with the Shiro framework class");
			sb.appendln("[without salt]");
			sb.appendln(" ------------------------------------- ");
			sb.appendln("The password hash with \"new String(hashedPassword)\":");
			sb.appendln("\t" + new String(passwordAsByteArray));
			sb.appendln(" ------------------------------------- ");
			sb.appendln("The password hash as hex representation:");
			sb.appendln("\t" + Hex.encodeToString(passwordAsByteArray));
			sb.appendln(" ------------------------------------- ");
			sb.appendln(
				"The password hash as hex representation created with the \"toHex()\"-method of the Shiro hash object");
			sb.appendln("\t" + hexEncoded);
		}
		else
		{
			/*
			 * Checkbox not checked -> hash with a salt
			 *
			 * The salt has to be stored along with the hash. It is necessary to validate a password!
			 */
			
			// Create a salt - for further dtails see documentation of the Shiro framework
			
			final RandomNumberGenerator rng  = new SecureRandomNumberGenerator();
			final ByteSource            salt = rng.nextBytes();
			
			// Get a Shiro Sha256Hash object - WITH salt
			final Sha256Hash sha256HashWithSalt = new Sha256Hash(this.mypassword, salt);
			
			// Get this hash as a byte array
			final byte[] passwordAsByteArray = sha256HashWithSalt.getBytes();
			
			// Get a hex representation with "toHex()"-method of the Shiro hash object
			final String hexEncoded = sha256HashWithSalt.toHex();
			
			sb.appendln(" ------------------------------------- ");
			sb.appendln("Hashing a password with the Shiro framework class");
			sb.appendln("[with salt: \"" + salt.toHex() + "\"]");
			sb.appendln(" ------------------------------------- ");
			sb.appendln("The password hash with \"new String(hashedPassword)\":");
			sb.appendln("\t" + new String(passwordAsByteArray));
			sb.appendln(" ------------------------------------- ");
			sb.appendln("The password hash as hex representation:");
			sb.appendln("\t" + Hex.encodeToString(passwordAsByteArray));
			sb.appendln(" ------------------------------------- ");
			sb.appendln(
				"The password hash as hex representation created with the \"toHex()\"-method of the Shiro hash object");
			sb.appendln("\t" + hexEncoded);
		}
		
		this.addToTextarea(sb.toString(), true);
		System.out.println(sb.toString());
	}
	
	/* WARNING: Do NOT edit!<br>The content of this method is always regenerated by the UI designer. */
	// <generated-code name="initUI">
	private void initUI()
	{
		this.verticalLayout        = new VerticalLayout();
		this.routerLink            = new RouterLink();
		this.hr                    = new Hr();
		this.formLayout            = new FormLayout();
		this.formItem              = new FormItem();
		this.label2                = new Label();
		this.lblDefaultCharset     = new Label();
		this.formItem2             = new FormItem();
		this.label3                = new Label();
		this.lblPassword           = new Label();
		this.accordion             = new Accordion();
		this.accordionPanel        = new AccordionPanel();
		this.verticalLayout3       = new VerticalLayout();
		this.labelRCIStrategyDesc  = new Label();
		this.labelRCIAlgo          = new Label();
		this.optionRCICondense     = new RadioButtonGroup<>();
		this.buttonRCIHash         = new Button();
		this.labelRCIDesc          = new Label();
		this.accordionPanel2       = new AccordionPanel();
		this.verticalLayout4       = new VerticalLayout();
		this.labelRCIStrategyDesc2 = new Label();
		this.labelRCIAlgo2         = new Label();
		this.optionRCIICondense    = new RadioButtonGroup<>();
		this.buttonRCII            = new Button();
		this.labelRCIIdesc         = new Label();
		this.accordionPanel3       = new AccordionPanel();
		this.verticalLayout5       = new VerticalLayout();
		this.labelRCIStrategyDesc3 = new Label();
		this.labelRCIAlgo3         = new Label();
		this.optionMDCondense      = new RadioButtonGroup<>();
		this.buttonMD              = new Button();
		this.labelMDdesc           = new Label();
		this.accordionPanel4       = new AccordionPanel();
		this.verticalLayout6       = new VerticalLayout();
		this.labelRCIStrategyDesc4 = new Label();
		this.labelRCIAlgo4         = new Label();
		this.chkSalt               = new Checkbox();
		this.buttonShiro           = new Button();
		this.labelShiroDesc        = new Label();
		this.verticalLayout2       = new VerticalLayout();
		this.textArea              = new TextArea();
		
		this.routerLink.setText("Back");
		if(!Beans.isDesignTime())
		{
			this.routerLink.setRoute(VaadinService.getCurrent().getRouter(), MainLayout.class);
		}
		this.label2.setText("Default Charset:");
		this.lblDefaultCharset.setText("Label");
		this.label3.setText("Password:");
		this.lblPassword.setText("Label");
		this.accordionPanel.setSummaryText("RapidClipse HashStrategy (I)");
		this.labelRCIStrategyDesc.setText("Charset: default charset");
		this.labelRCIAlgo.setText("Algorithm: SHA256");
		this.optionRCICondense
			.setRenderer(new TextRenderer<>(ItemLabelGeneratorFactory.NonNull(CaptionUtils::resolveCaption)));
		this.buttonRCIHash.setText("show");
		this.labelRCIDesc.setText("Hash with the HashStrategy from the AuthenticationProvider.");
		this.accordionPanel2.setSummaryText("RapidClipse HashStrategy (II)");
		this.labelRCIStrategyDesc2.setText("Charset: default charset");
		this.labelRCIAlgo2.setText("Algorithm: SHA256");
		this.optionRCIICondense
			.setRenderer(new TextRenderer<>(ItemLabelGeneratorFactory.NonNull(CaptionUtils::resolveCaption)));
		this.buttonRCII.setText("show");
		this.labelRCIIdesc.setText("Hash with a new HashStrategy");
		this.accordionPanel3.setSummaryText("MessageDigest");
		this.labelRCIStrategyDesc3.setText("Charset: default charset");
		this.labelRCIAlgo3.setText("Algorithm: SHA256");
		this.optionMDCondense
			.setRenderer(new TextRenderer<>(ItemLabelGeneratorFactory.NonNull(CaptionUtils::resolveCaption)));
		this.buttonMD.setText("show");
		this.labelMDdesc.setText("Hash a password with a new MessageDigest object");
		this.accordionPanel4.setSummaryText(" Shiro framework");
		this.labelRCIStrategyDesc4.setText("Charset: default charset");
		this.labelRCIAlgo4.setText("Algorithm: SHA256");
		this.chkSalt.setLabel("with salt");
		this.buttonShiro.setText("show");
		this.labelShiroDesc.setText("With SHA256 class");
		
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
		this.labelRCIStrategyDesc.setSizeUndefined();
		this.labelRCIAlgo.setSizeUndefined();
		this.buttonRCIHash.setSizeUndefined();
		this.labelRCIDesc.setSizeUndefined();
		this.verticalLayout3.add(this.labelRCIStrategyDesc, this.labelRCIAlgo, this.optionRCICondense,
			this.buttonRCIHash,
			this.labelRCIDesc);
		this.verticalLayout3.setSizeFull();
		this.accordionPanel.setContent(this.verticalLayout3);
		this.labelRCIStrategyDesc2.setSizeUndefined();
		this.labelRCIAlgo2.setSizeUndefined();
		this.buttonRCII.setSizeUndefined();
		this.labelRCIIdesc.setSizeUndefined();
		this.verticalLayout4.add(this.labelRCIStrategyDesc2, this.labelRCIAlgo2, this.optionRCIICondense,
			this.buttonRCII,
			this.labelRCIIdesc);
		this.verticalLayout4.setSizeFull();
		this.accordionPanel2.setContent(this.verticalLayout4);
		this.labelRCIStrategyDesc3.setSizeUndefined();
		this.labelRCIAlgo3.setSizeUndefined();
		this.buttonMD.setSizeUndefined();
		this.labelMDdesc.setSizeUndefined();
		this.verticalLayout5.add(this.labelRCIStrategyDesc3, this.labelRCIAlgo3, this.optionMDCondense, this.buttonMD,
			this.labelMDdesc);
		this.verticalLayout5.setSizeFull();
		this.accordionPanel3.setContent(this.verticalLayout5);
		this.labelRCIStrategyDesc4.setSizeUndefined();
		this.labelRCIAlgo4.setSizeUndefined();
		this.chkSalt.setSizeUndefined();
		this.buttonShiro.setSizeUndefined();
		this.labelShiroDesc.setSizeUndefined();
		this.verticalLayout6.add(this.labelRCIStrategyDesc4, this.labelRCIAlgo4, this.chkSalt, this.buttonShiro,
			this.labelShiroDesc);
		this.verticalLayout6.setSizeFull();
		this.accordionPanel4.setContent(this.verticalLayout6);
		this.accordion.add(this.accordionPanel);
		this.accordion.add(this.accordionPanel2);
		this.accordion.add(this.accordionPanel3);
		this.accordion.add(this.accordionPanel4);
		this.accordion.open(3);
		this.hr.setWidthFull();
		this.hr.setHeight(null);
		this.formLayout.setWidthFull();
		this.formLayout.setHeight(null);
		this.accordion.setWidthFull();
		this.accordion.setHeight(null);
		this.verticalLayout.add(this.routerLink, this.hr, this.formLayout, this.accordion);
		this.textArea.setSizeFull();
		this.verticalLayout2.add(this.textArea);
		this.addToPrimary(this.verticalLayout);
		this.addToSecondary(this.verticalLayout2);
		this.setSplitterPosition(50.0);
		this.setSizeFull();
		
		this.buttonRCIHash.addClickListener(this::buttonRCIHash_onClick);
		this.buttonRCII.addClickListener(this::buttonRCII_onClick);
		this.buttonMD.addClickListener(this::buttonMD_onClick);
		this.buttonShiro.addClickListener(this::buttonShiro_onClick);
	} // </generated-code>
	
	// <generated-code name="variables">
	private FormLayout               formLayout;
	private Accordion                accordion;
	private Checkbox                 chkSalt;
	private Button                   buttonRCIHash, buttonRCII, buttonMD, buttonShiro;
	private TextArea                 textArea;
	private VerticalLayout           verticalLayout, verticalLayout3, verticalLayout4, verticalLayout5, verticalLayout6,
		verticalLayout2;
	private RouterLink               routerLink;
	private Label                    label2, lblDefaultCharset, label3, lblPassword, labelRCIStrategyDesc, labelRCIAlgo,
		labelRCIDesc, labelRCIStrategyDesc2, labelRCIAlgo2, labelRCIIdesc, labelRCIStrategyDesc3, labelRCIAlgo3,
		labelMDdesc, labelRCIStrategyDesc4, labelRCIAlgo4, labelShiroDesc;
	private Hr                       hr;
	private AccordionPanel           accordionPanel, accordionPanel2, accordionPanel3, accordionPanel4;
	private RadioButtonGroup<String> optionRCICondense, optionRCIICondense, optionMDCondense;
	private FormItem                 formItem, formItem2;
	// </generated-code>
	
}

/**
 * Copyright 2011 Cheng Wei, Robert Taylor
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 */
package org.robobinding.viewattribute;

import org.robobinding.binders.BindingContext;
import org.robobinding.presentationmodel.PresentationModelAdapter;
import org.robobinding.viewattribute.view.ViewListeners;
import org.robobinding.viewattribute.view.ViewListenersAware;

import android.view.View;

/**
 *
 * @since 1.0
 * @version $Revision: 1.0 $
 * @author Robert Taylor
 */
public abstract class AbstractMultiTypePropertyViewAttribute<T extends View> implements PropertyViewAttribute<T>
{
	private T view;
	private PropertyBindingDetails propertyBindingDetails;
	private ViewListenersProvider viewListenersProvider;
	
	@Override
	public void setView(T view)
	{
		this.view = view;
	}
	@Override
	public void setAttributeValue(String attributeValue)
	{
		this.propertyBindingDetails = PropertyBindingDetails.createFrom(attributeValue);
	}
	
	public void setViewListenersProvider(ViewListenersProvider viewListenersProvider)
	{
		this.viewListenersProvider = viewListenersProvider;
	}
	
	public boolean isTwoWayBinding()
	{
		return propertyBindingDetails.twoWayBinding;
	}
	
	@Override
	public void bindTo(BindingContext context)
	{
		AbstractPropertyViewAttribute<T, ?> propertyViewAttribute = lookupPropertyViewAttribute(context.getPresentationModelAdapter());
		propertyViewAttribute.setView(view);
		propertyViewAttribute.setPropertyBindingDetails(propertyBindingDetails);
		setViewListenersIfRequired(propertyViewAttribute, view);
		propertyViewAttribute.bindTo(context);
	}

	private void setViewListenersIfRequired(ViewAttribute viewAttribute, View view)
	{
		if(viewAttribute instanceof ViewListenersAware)
		{
			ViewListeners viewListeners = viewListenersProvider.forViewAndAttribute(view, (ViewListenersAware<?>)viewAttribute);
			@SuppressWarnings("unchecked")
			ViewListenersAware<ViewListeners> viewListenersAware = (ViewListenersAware<ViewListeners>)viewAttribute;
			viewListenersAware.setViewListeners(viewListeners);
		}
	}
	
	protected abstract AbstractPropertyViewAttribute<T, ?> createPropertyViewAttribute(Class<?> propertyType);

	private AbstractPropertyViewAttribute<T, ?> lookupPropertyViewAttribute(PresentationModelAdapter presentationModelAdapter)
	{
		Class<?> propertyType = presentationModelAdapter.getPropertyType(propertyBindingDetails.propertyName);
		AbstractPropertyViewAttribute<T, ?> propertyViewAttribute = createPropertyViewAttribute(propertyType);
		
		if (propertyViewAttribute == null)
			throw new RuntimeException("Could not find a suitable attribute in " + getClass().getName() + " for property type: " + propertyType);
		
		return propertyViewAttribute;
	}
}

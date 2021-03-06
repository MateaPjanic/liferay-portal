/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.adaptive.media.image.optimizer;

import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author Sergio González
 */
@Component(immediate = true)
public class AdaptiveMediaImageOptimizerUtil {

	public static void optimize(long companyId) {
		if (_serviceTrackerMap == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Cannot optimize for company id " + companyId +
						" because the component is not actived");
			}

			return;
		}

		Set<String> modelClassNames = _serviceTrackerMap.keySet();

		for (String modelClassName : modelClassNames) {
			AdaptiveMediaImageOptimizer adaptiveMediaImageOptimizer =
				_serviceTrackerMap.getService(modelClassName);

			adaptiveMediaImageOptimizer.optimize(companyId);
		}
	}

	public static void optimize(long companyId, String configurationEntryUuid) {
		if (_serviceTrackerMap == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Cannot optimize for company id " + companyId +
						" because the component is not actived");
			}

			return;
		}

		Set<String> modelClassNames = _serviceTrackerMap.keySet();

		for (String modelClassName : modelClassNames) {
			AdaptiveMediaImageOptimizer adaptiveMediaImageOptimizer =
				_serviceTrackerMap.getService(modelClassName);

			adaptiveMediaImageOptimizer.optimize(
				companyId, configurationEntryUuid);
		}
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_serviceTrackerMap = ServiceTrackerMapFactory.openSingleValueMap(
			bundleContext, AdaptiveMediaImageOptimizer.class,
			"adaptive.media.key");
	}

	@Deactivate
	protected void deactivate() {
		_serviceTrackerMap.close();

		_serviceTrackerMap = null;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AdaptiveMediaImageOptimizerUtil.class);

	private static ServiceTrackerMap<String, AdaptiveMediaImageOptimizer>
		_serviceTrackerMap;

}
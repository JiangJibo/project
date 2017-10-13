/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.config.mvc.validator;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

import com.bob.config.mvc.validator.ValidationGroupSequence.FieldSequence;
import com.bob.config.mvc.validator.ValidationGroupSequence.MethodSequence;

/**
 * @since 2017年6月8日 下午9:15:30
 * @version $Id$
 * @author JiangJibo
 *
 */
@GroupSequence(value = { Default.class, FieldSequence.class, MethodSequence.class })
public class ValidationGroupSequence {

	interface FieldSequence {

	}

	interface MethodSequence {

	}

}

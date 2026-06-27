package it.unicam.cs.mpgc.rpg130398.api.dialog;

import java.util.Set;

/**
 * View of some Dialog parameters, exposed to {@link ConnectionRequirement} implementations,
 * to permit {@link ConnectionRequirement} to evaluate themselves without depending on the
 * concrete {@link Dialog} implementation.
 * <p>
 * This interface intentionally exposes no mutators: a Requirement must only be
 * able to inspect the state, never change it.
 */
public interface DialogState { }
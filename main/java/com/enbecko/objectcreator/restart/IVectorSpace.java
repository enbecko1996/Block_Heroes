package com.enbecko.objectcreator.restart;

import org.ojalgo.matrix.store.MatrixStore;

/**
 * Created by Niclas on 19.11.2016.
 */
public interface IVectorSpace {

    double getOneUnit();

    MatrixStore<Double> getTransform();
}

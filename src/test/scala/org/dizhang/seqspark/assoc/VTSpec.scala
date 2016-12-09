package org.dizhang.seqspark.assoc

import breeze.linalg.{DenseMatrix, DenseVector, sum}
import breeze.stats.distributions.{Binomial, Gaussian, RandBasis, ThreadLocalRandomGenerator}
import com.typesafe.config.ConfigFactory
import org.apache.commons.math3.random.MersenneTwister
import org.scalatest.FlatSpec
import org.dizhang.seqspark.ds.{Genotype, Variant}
import org.dizhang.seqspark.stat.{LinearRegression, ScoreTest}
import org.dizhang.seqspark.util.UserConfig.MethodConfig
import org.dizhang.seqspark.util.Constant
/**
  * Created by zhangdi on 10/11/16.
  */
class VTSpec extends FlatSpec {
  val randBasis: RandBasis = new RandBasis(new ThreadLocalRandomGenerator(new MersenneTwister(100)))

  val encode = {
    val conf = ConfigFactory.load().getConfig("seqspark.association.method.vt")
    val method = MethodConfig(conf)
    val randg = Binomial(3, 0.02)(randBasis)
    val gt = Array("0/0", "0/1", "1/1", "./.")
    val vars = (0 to 10).map{i =>
      val meta = Array("1", i.toString, ".", "A", "C", ".", ".", ".")
      val geno = randg.sample(2000).map(g => Genotype.Raw.toSimpleGenotype(gt(g)))
      Variant.fromIndexedSeq(meta, geno, 16.toByte)
    }
    val sm = method.config.root().render()
    Encode(vars, sm)
  }
  val ec2 = {
    val conf = ConfigFactory.load().getConfig("seqspark.association.method.vt")
    val method = MethodConfig(conf)
    val gt = Array("0/0", "0/1", "1/1", "./.")
    val meta = Array("1", "2", ".", "A", "C", ".", ".", ".")
    val vs = Array(
      Variant.fromIndexedSeq(meta, Array(1,0,0).map(g => Genotype.Raw.toSimpleGenotype(gt(g))), 16.toByte),
      Variant.fromIndexedSeq(meta, Array(0,0,1).map(g => Genotype.Raw.toSimpleGenotype(gt(g))), 16.toByte),
      Variant.fromIndexedSeq(meta, Array(0,2,0).map(g => Genotype.Raw.toSimpleGenotype(gt(g))), 16.toByte)
    )
    val sm = method.config.root().render()
    Encode(vs, sm)
  }
  val nullModel = {
    val rand = Gaussian(2, 0.25)(randBasis)
    val dat = (0 to 3).map{i =>
      rand.sample(2000)
    }
    val y = DenseVector(rand.sample(2000): _*)
    val dm = DenseMatrix(dat: _*)
    val reg = LinearRegression(y, dm.t)
    ScoreTest.NullModel(reg)
  }
  //"A VT" should "be fine" in {
    //println(s"defined: ${encode.isDefined} informative: ${encode.informative()} mut: ${sum(encode.getFixed.coding)}")
    //val vt = VT(nullModel, encode.getVT)
    //println(s"S: ${vt.statistic} P: ${vt.pValue}")
  //}
}

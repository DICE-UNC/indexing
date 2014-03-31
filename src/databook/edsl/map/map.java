/** Code generated by EriLex */
package databook.edsl.map;
public class map<Kappa,Sigma,__t,__E> {
	F<Dmap<__t,__E>,Sigma> b;
	F<Sigma,Kappa> k;
	public map(
		final F<Dmap<__t,__E>,Sigma> b, 
		final F<Sigma,Kappa> k) {
		this.b=b;
		this.k=k;
	}
	public map<Kappa,Sigma,__t,__E> dataObject() {
		return new F<F<Dmap<__t,__E>,Sigma>,map<Kappa,Sigma,__t,__E>>() {
			public map<Kappa,Sigma,__t,__E> APP(final F<Dmap<__t,__E>,Sigma> b1) {
				return new map(
					b1, 
					k);
			}
		}.APP(new F<Dmap<__t,__E>,Sigma>() {
			public Sigma APP(final Dmap<__t,__E> t1) {
				return b.APP(new DmapDdataObject(
					t1));
			}
		});
	}
	public mapValue<Kappa,Sigma,__t,__E> key(
		final java.lang.String i1) {
		return new F<F<DmapValue<__t,__E>,Sigma>,mapValue<Kappa,Sigma,__t,__E>>() {
			public mapValue<Kappa,Sigma,__t,__E> APP(final F<DmapValue<__t,__E>,Sigma> b1) {
				return new mapValue(
					b1, 
					k);
			}
		}.APP(new F<DmapValue<__t,__E>,Sigma>() {
			public Sigma APP(final DmapValue<__t,__E> t1) {
				return b.APP(new DmapDkey(
					i1, 
					t1));
			}
		});
	}
	public loc<map<Kappa,Sigma,__t,__E>,F<Dmap<__t,__E>,Sigma>,__t,__E> at() {
		return new F<F<Dloc<__t,__E>,F<Dmap<__t,__E>,Sigma>>,loc<map<Kappa,Sigma,__t,__E>,F<Dmap<__t,__E>,Sigma>,__t,__E>>() {
			public loc<map<Kappa,Sigma,__t,__E>,F<Dmap<__t,__E>,Sigma>,__t,__E> APP(final F<Dloc<__t,__E>,F<Dmap<__t,__E>,Sigma>> b2) {
				return new loc(
					b2, 
					new F<F<Dmap<__t,__E>,Sigma>,map<Kappa,Sigma,__t,__E>>() {
						public map<Kappa,Sigma,__t,__E> APP(final F<Dmap<__t,__E>,Sigma> b1) {
							return new map(
								b1, 
								k);
						}
					});
			}
		}.APP(new F<Dloc<__t,__E>,F<Dmap<__t,__E>,Sigma>>() {
			public F<Dmap<__t,__E>,Sigma> APP(final Dloc<__t,__E> t1) {
				return new F<Dmap<__t,__E>,Sigma>() {
					public Sigma APP(final Dmap<__t,__E> t2) {
						return b.APP(new DmapDat(
							t1, 
							t2));
					}
				};
			}
		});
	}
	public Kappa end() {
		return k.APP(b.APP(new DmapDend()));
	}
	public map<Kappa,Sigma,__t,__E> collection() {
		return new F<F<Dmap<__t,__E>,Sigma>,map<Kappa,Sigma,__t,__E>>() {
			public map<Kappa,Sigma,__t,__E> APP(final F<Dmap<__t,__E>,Sigma> b1) {
				return new map(
					b1, 
					k);
			}
		}.APP(new F<Dmap<__t,__E>,Sigma>() {
			public Sigma APP(final Dmap<__t,__E> t1) {
				return b.APP(new DmapDcollection(
					t1));
			}
		});
	}
	public arrayTail<map<Kappa,Sigma,__t,__E>,F<Dmap<__t,__E>,Sigma>,__t,__E> hasPart() {
		return new F<F<DarrayTail<__t,__E>,F<Dmap<__t,__E>,Sigma>>,arrayTail<map<Kappa,Sigma,__t,__E>,F<Dmap<__t,__E>,Sigma>,__t,__E>>() {
			public arrayTail<map<Kappa,Sigma,__t,__E>,F<Dmap<__t,__E>,Sigma>,__t,__E> APP(final F<DarrayTail<__t,__E>,F<Dmap<__t,__E>,Sigma>> b2) {
				return new arrayTail(
					b2, 
					new F<F<Dmap<__t,__E>,Sigma>,map<Kappa,Sigma,__t,__E>>() {
						public map<Kappa,Sigma,__t,__E> APP(final F<Dmap<__t,__E>,Sigma> b1) {
							return new map(
								b1, 
								k);
						}
					});
			}
		}.APP(new F<DarrayTail<__t,__E>,F<Dmap<__t,__E>,Sigma>>() {
			public F<Dmap<__t,__E>,Sigma> APP(final DarrayTail<__t,__E> t1) {
				return new F<Dmap<__t,__E>,Sigma>() {
					public Sigma APP(final Dmap<__t,__E> t2) {
						return b.APP(new DmapDhasPart(
							t1, 
							t2));
					}
				};
			}
		});
	}
	public map<Kappa,Sigma,__t,__E> uri(
		final java.lang.String i1) {
		return new F<F<Dmap<__t,__E>,Sigma>,map<Kappa,Sigma,__t,__E>>() {
			public map<Kappa,Sigma,__t,__E> APP(final F<Dmap<__t,__E>,Sigma> b1) {
				return new map(
					b1, 
					k);
			}
		}.APP(new F<Dmap<__t,__E>,Sigma>() {
			public Sigma APP(final Dmap<__t,__E> t1) {
				return b.APP(new DmapDuri(
					i1, 
					t1));
			}
		});
	}
	public map<Kappa,Sigma,__t,__E> label(
		final java.lang.String i1) {
		return new F<F<Dmap<__t,__E>,Sigma>,map<Kappa,Sigma,__t,__E>>() {
			public map<Kappa,Sigma,__t,__E> APP(final F<Dmap<__t,__E>,Sigma> b1) {
				return new map(
					b1, 
					k);
			}
		}.APP(new F<Dmap<__t,__E>,Sigma>() {
			public Sigma APP(final Dmap<__t,__E> t1) {
				return b.APP(new DmapDlabel(
					i1, 
					t1));
			}
		});
	}
}

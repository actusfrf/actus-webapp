(window.webpackJsonp=window.webpackJsonp||[]).push([[0],{110:function(e,t,a){},111:function(e,t,a){},131:function(e,t,a){},138:function(e,t,a){},140:function(e,t,a){},141:function(e,t,a){},142:function(e,t,a){},143:function(e,t,a){},144:function(e,t,a){},145:function(e,t,a){},146:function(e,t,a){"use strict";a.r(t);var n=a(1),r=a.n(n),c=a(27),i=a.n(c),l=a(153);Boolean("localhost"===window.location.hostname||"[::1]"===window.location.hostname||window.location.hostname.match(/^127(?:\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}$/));var s=a(3),o=a(4),m=a(6),u=a(5),d=a(7),p=a(154),h=a(155),f=a(148),E=a(149),g=a(150),b=(a(73),function(e){function t(){return Object(s.a)(this,t),Object(m.a)(this,Object(u.a)(t).apply(this,arguments))}return Object(d.a)(t,e),Object(o.a)(t,[{key:"render",value:function(){return r.a.createElement("div",{className:"header-wrapper"},r.a.createElement("a",{href:"https://actusfrf.org",target:"_blank"},r.a.createElement("img",{src:"https://raw.githubusercontent.com/actusfrf/actus-resources/master/logos/actus_logo.jpg",width:"200",alt:"ACTUS Logo"})))}}]),t}(n.PureComponent)),A=(a(74),function(e){function t(){return Object(s.a)(this,t),Object(m.a)(this,Object(u.a)(t).apply(this,arguments))}return Object(d.a)(t,e),Object(o.a)(t,[{key:"render",value:function(){return r.a.createElement("div",{className:"footer-wrapper"},r.a.createElement("p",null,"\xa9 2019 - present ",r.a.createElement("a",{href:"https://actusfrf.org",target:"_blank"}," ACTUS Financial Research Foundation ")," "))}}]),t}(n.PureComponent)),v=(a(75),function(e){function t(){var e,a;Object(s.a)(this,t);for(var n=arguments.length,r=new Array(n),c=0;c<n;c++)r[c]=arguments[c];return(a=Object(m.a)(this,(e=Object(u.a)(t)).call.apply(e,[this].concat(r)))).displayName=t.name,a}return Object(d.a)(t,e),Object(o.a)(t,[{key:"render",value:function(){return r.a.createElement(f.a,null,r.a.createElement(E.a,null,r.a.createElement(g.a,{sm:12},r.a.createElement("div",{className:"main-container"},r.a.createElement(b,null),this.props.children,r.a.createElement(A,null)))))}}]),t}(n.Component)),y=a(15),O=a(21),D=a(12),j=a(152),N=(a(110),a(65)),w=a.n(N),F=function(e){function t(){return Object(s.a)(this,t),Object(m.a)(this,Object(u.a)(t).apply(this,arguments))}return Object(d.a)(t,e),Object(o.a)(t,[{key:"render",value:function(){var e=this.props.description;return r.a.createElement("i",{className:"tool-tip"},r.a.createElement("img",{src:w.a,alt:"Information"}),r.a.createElement("div",{className:"tool-tip-copy"},e))}}]),t}(n.PureComponent),k=(a(111),n.PureComponent,a(20)),C=a.n(k),R=(a(131),function(e){function t(){var e,a;Object(s.a)(this,t);for(var n=arguments.length,r=new Array(n),c=0;c<n;c++)r[c]=arguments[c];return(a=Object(m.a)(this,(e=Object(u.a)(t)).call.apply(e,[this].concat(r)))).state={fields:[],label:"",identifier:"",terms:{},demoId:"",passDemoData:null},a}return Object(d.a)(t,e),Object(o.a)(t,[{key:"passDemoData",value:function(){this.state.passDemoData(this.state.terms,this.state.demoId)}},{key:"componentDidMount",value:function(){this.setState({terms:this.props.terms,identifier:this.props.identifier,demoId:this.props.demoId,label:this.props.label,passDemoData:this.props.passDemoData})}},{key:"render",value:function(){var e=this,t=this.props,a=t.description,n=t.index;return r.a.createElement(g.a,{sm:4,className:"demo-col"},r.a.createElement("div",{className:"demo-item"},r.a.createElement("div",{className:"demo-item-content"},r.a.createElement("h4",{onClick:function(t){return e.passDemoData()},className:"demo-case-title"},"Case ",n+1),r.a.createElement("article",{className:"demo-text"},null!==a?a:"No Description"))))}}]),t}(n.PureComponent)),M=(a(132),a(137),a(138),function(e){function t(){var e,a;Object(s.a)(this,t);for(var n=arguments.length,r=new Array(n),c=0;c<n;c++)r[c]=arguments[c];return(a=Object(m.a)(this,(e=Object(u.a)(t)).call.apply(e,[this].concat(r)))).state={groups:[],error:!1,startDate:new Date,validated:!1,optionalFields:[],mandatoryFields:[],originalRequiredFields:{},originalNonRequiredFields:{},requiredFields:{},nonRequiredFields:{},demos:[],showDemos:!1,showForm:!0,totalFields:0,groupDescription:"",contractType:"",identifier:"",version:"",results:{},isFetching:!1,redirect:!1,host:"localhost:8080"},a}return Object(d.a)(t,e),Object(o.a)(t,[{key:"assemble",value:function(e,t){return e.Group[t.Name]=t.Name,e}},{key:"getTestFields",value:function(){return{ContractType:"PAM",StatusDate:"2015-01-01T00:00:00",ContractRole:"RPA",ContractID:101,LegalEntityIDCounterparty:"TEST_LEI_CP",NominalInterestRate:0,DayCountConvention:"30E/360",CyclePointOfInterestPayment:1,Currency:"USD",ContractDealDate:"2015-01-01T00:00:00",InitialExchangeDate:"2015-01-02T00:00:00",MaturityDate:"2015-04-02T00:00:00",NotionalPrincipal:1e3,RateSpread:0,CyclePointOfRateReset:0,RateMultiplier:1,MarketValueObserved:10,PremiumDiscountAtIED:-5}}},{key:"handleChange",value:function(e){this.setState({startDate:e})}},{key:"handleReset",value:function(e){console.log("Reset")}},{key:"cleanUpData",value:function(e){for(var t in e)""===e[t]&&delete e[t];return e}},{key:"handleSubmit",value:function(e){var t=this;e.preventDefault();var a=Object.assign({},this.state.requiredFields,this.state.nonRequiredFields),n=Object(D.a)({},a);console.log("Cleaned Up:",this.cleanUpData(n)),console.log("Test data:",this.getTestFields()),C.a.post(this.state.host+"/events",this.cleanUpData(n)).then(function(e){t.setState({results:e.data,isFetching:!1,redirect:!0})}).catch(function(e){console.log(t),console.log(e.message)})}},{key:"validateFields",value:function(){var e=this.state.requiredFields,t=0;for(var a in e)""===e[a]&&t++;this.setState({validated:0===t})}},{key:"updateNonRequiredField",value:function(e){this.setState({nonRequiredFields:Object(D.a)({},this.state.nonRequiredFields,Object(O.a)({},e.target.id,e.target.value))})}},{key:"updateField",value:function(e){this.setState({requiredFields:Object(D.a)({},this.state.requiredFields,Object(O.a)({},e.target.id,e.target.value))}),this.validateFields()}},{key:"onGroupUpdate",value:function(e){console.log(e.target.id),console.log(e.target.title),console.log(e.target.group)}},{key:"componentDidMount",value:function(){console.log("Did Mount");var e=this.props.match;this.fetchTerms(e.params.id)}},{key:"fetchTerms",value:function(e){var t=this;this.setState({isFetching:!0}),C.a.get("".concat(this.state.host,"/terms/meta/").concat(e)).then(function(a){if(!a.data[0].terms||!a||!a.data)return!1;var n=a.data[0],r=a.data[0].terms.filter(function(e){return e.applicability.indexOf("NN")<=-1}),c=a.data[0].terms.filter(function(e){return e.applicability.indexOf("NN")>-1}),i=Object.assign.apply(Object,[{}].concat(Object(y.a)(c.map(function(e){return Object(O.a)({},e.name,"")})))),l=Object.assign.apply(Object,[{}].concat(Object(y.a)(r.map(function(e){return Object(O.a)({},e.name,"")})))),s=r.reduce(function(e,t){return e[t.group]=e[t.group]||[],e[t.group].push(t),e},Object.create(null)),o={};r.map(function(e,t){o[e.name]={},o[e.name]=""});var m=Object.keys(s).map(function(e){return{group:e,Items:s[e]}});t.fetchDemos(e),t.setState({groups:m,isFetching:!1,optionalFields:Object(y.a)(r),mandatoryFields:Object(y.a)(c),requiredFields:Object(D.a)({},i),nonRequiredFields:Object(D.a)({},l),originalRequiredFields:Object(D.a)({},i),originalNonRequiredFields:Object(D.a)({},l),totalFields:Object.keys(o).length,groupDescription:n.description,contractType:n.contractType,identifier:n.identifier,version:n.version,error:Object(D.a)({},t.state.error)})}).catch(function(e){console.log(">>>>>>>>>>> error:",e)})}},{key:"fetchDemos",value:function(e){var t=this;C.a.get("".concat(this.state.host,"/demos/meta/").concat(e)).then(function(e){t.setState({demos:e.data}),console.log("%cdemos:","background: black; padding:1rem; color: #fff",e.data),console.log(e.data.filter(function(e){return"101"===e.terms.ContractID.toString()}))}).catch(function(e){console.log(">>>>>>>>>>> error:",e)})}},{key:"toggleDemos",value:function(){this.setState({showDemos:!this.state.showDemos})}},{key:"toggleForm",value:function(){this.setState({showForm:!this.state.showForm})}},{key:"passDemoData",value:function(e,t){console.log(e.ContractID);var a=Object(y.a)(this.state.groups),n=Object(D.a)({},this.state.originalNonRequiredFields),r=Object(D.a)({},this.state.originalRequiredFields),c=Object.entries(e),i=Object.entries(r);c.map(function(e){i.map(function(t){e[0]==t[0]&&(r[e[0]]=e[1])})}),a.map(function(e){e.Items.map(function(e){c.map(function(t){t[0]==e.name&&(n[t[0]]=t[1])})})}),this.setState({requiredFields:Object(D.a)({},this.state.originalRequiredFields,r),nonRequiredFields:Object(D.a)({},this.state.originalNonRequiredFields,n)})}},{key:"render",value:function(){var e=this,t=this.state,a=t.groups,n=t.groupDescription,c=t.contractType,i=t.identifier,l=t.version,s=t.mandatoryFields,o=t.redirect,m=t.results,u=t.demos,d=this.state.showDemos?"unfolded":"folded",p=this.state.showForm?"unfolded":"folded";return o?r.a.createElement(j.a,{to:{pathname:"/results",state:{data:m}}}):this.state.isFetching?r.a.createElement("div",null,"Loading..."):r.a.createElement("div",{id:"form-container",identifier:i,version:l},r.a.createElement(f.a,{fluid:!0},r.a.createElement(E.a,null,r.a.createElement(g.a,{sm:12,className:"contract-main-wrapper demo ".concat(d),onClick:function(){return e.toggleDemos()}},r.a.createElement("span",{className:"contract-title"},"Demo Case"))),this.state.showDemos&&r.a.createElement(E.a,null,r.a.createElement(g.a,{sm:12,className:"demo-items-wrapper"},r.a.createElement(f.a,{fluid:!0},r.a.createElement(E.a,{className:"demo-row"},u.map(function(t,a){return r.a.createElement(R,{key:a,passDemoData:e.passDemoData.bind(e),description:t.description,identifier:t.identifier,index:a,demoId:t.id,label:t.label,terms:t.terms})}))))),r.a.createElement(E.a,null,r.a.createElement(g.a,{sm:12,className:"contract-main-wrapper ".concat(p),onClick:function(){return e.toggleForm()}},r.a.createElement("span",{className:"contract-title"},c,":"),r.a.createElement("span",{className:"contract-description"},n))),this.state.showForm&&r.a.createElement(E.a,null,r.a.createElement(g.a,{sm:5,className:"required choices"},r.a.createElement("div",{className:"term-group-header"},"All fields below are mandatory to fill in:"),r.a.createElement("div",{className:"field-wrapper"},r.a.createElement("div",{className:"items-group"},r.a.createElement(f.a,{fluid:!0},r.a.createElement(E.a,null,s.map(function(t,a){var n=t.name;return n=(n=n.replace(/([a-z])([A-Z])/g,"$1 $2")).replace(/([A-Z])([A-Z])/g,"$1 $2"),r.a.createElement(g.a,{key:"term_wrapper".concat(a),sm:6,className:"item nopadding"},r.a.createElement("div",{className:"input-container"},r.a.createElement("label",{className:"item-labels",htmlFor:t.name},n),r.a.createElement("div",{className:"input-wrapper"},r.a.createElement("input",{id:t.name,applicability:t.applicability,title:"Required Field",placeholder:"...",value:e.state.requiredFields[t.name],onChange:function(t){return e.updateField(t)},className:"item-fields",type:"text"}),r.a.createElement(F,{description:t.description}))))})))))),r.a.createElement(g.a,{sm:7,className:"optional choices"},r.a.createElement("div",{className:"term-group-header"},"Below are your Optional choices"),a.map(function(t,a){return r.a.createElement("div",{key:"term_wrapper".concat(a),className:"term-wrapper"},r.a.createElement("div",{id:t,className:"items-group"},r.a.createElement("div",{className:"item-header"},t.group),r.a.createElement(f.a,{fluid:!0},r.a.createElement(E.a,null,t.Items.map(function(t,a){var n=t.name,c=t.group;return n=(n=n.replace(/([a-z])([A-Z])/g,"$1 $2")).replace(/([A-Z])([A-Z])/g,"$1 $2"),r.a.createElement(g.a,{key:"key".concat(t.name),sm:4,className:"item nopadding"},r.a.createElement("div",{className:"input-container"},r.a.createElement("label",{className:"item-labels",htmlFor:t.name},n),r.a.createElement("div",{className:"input-wrapper term"},r.a.createElement("input",{id:t.name,group:c,applicability:t.applicability,title:"Optional Choice",placeholder:"...",value:e.state.nonRequiredFields[t.name],onChange:function(t){return e.updateNonRequiredField(t)},className:"item-fields",type:"text"}),r.a.createElement(F,{description:t.description}))))})))))}))),r.a.createElement(E.a,null,r.a.createElement(g.a,{sm:6,className:"text-right"},r.a.createElement("input",{type:"reset",value:"RESET",onClick:function(t){return e.handleReset(t)}})),r.a.createElement(g.a,{sm:6,className:(this.state.validated,"text-left")},r.a.createElement("input",{type:"submit",value:"SEND",onClick:function(t){return e.handleSubmit(t)}})))))}}]),t}(n.PureComponent)),I=(a(140),a(141),function(e){function t(){var e,a;Object(s.a)(this,t);for(var n=arguments.length,r=new Array(n),c=0;c<n;c++)r[c]=arguments[c];return(a=Object(m.a)(this,(e=Object(u.a)(t)).call.apply(e,[this].concat(r)))).state={data:[]},a}return Object(d.a)(t,e),Object(o.a)(t,[{key:"componentDidMount",value:function(){this.setState({data:Object(y.a)(this.props.data)})}},{key:"render",value:function(){return r.a.createElement("div",null,r.a.createElement("div",{className:"text-center"},"TODO: GRAPH HERE"),r.a.createElement("canvas",{id:"graphRender",className:"graph-render",width:"680",height:"480"}))}}]),t}(n.PureComponent)),S=function(e){function t(){var e,a;Object(s.a)(this,t);for(var n=arguments.length,r=new Array(n),c=0;c<n;c++)r[c]=arguments[c];return(a=Object(m.a)(this,(e=Object(u.a)(t)).call.apply(e,[this].concat(r)))).state={results:[],currentTab:"Table"},a}return Object(d.a)(t,e),Object(o.a)(t,[{key:"componentDidMount",value:function(){var e=Object(y.a)(this.props.location.state.data);this.setState({results:e})}},{key:"render",value:function(){var e=this.state,t=e.results,a=e.currentTab;return r.a.createElement("div",{id:"results-container",identifier:"",version:""},r.a.createElement(f.a,{fluid:!0},r.a.createElement(E.a,null,r.a.createElement(g.a,{sm:12,className:"results-main-wrapper"},r.a.createElement("span",{className:"results-title"},"RESULT:"),r.a.createElement("span",{className:"results-description"},"group description"))),r.a.createElement(E.a,null,r.a.createElement(g.a,{sm:12,className:"results-graph-wrapper"},r.a.createElement(I,{data:t}))),"Table"===a&&r.a.createElement(E.a,null,r.a.createElement(g.a,{sm:12},r.a.createElement(f.a,{fluid:!0,className:"results-table-wrapper"},r.a.createElement(E.a,{className:"table-header-container"},r.a.createElement(g.a,{sm:2,className:"table-header"},"Event Date"),r.a.createElement(g.a,{sm:1,className:"table-header"},"Event Type"),r.a.createElement(g.a,{sm:2,className:"table-header"},"Event Value"),r.a.createElement(g.a,{sm:2,className:"table-header"},"Event Currency"),r.a.createElement(g.a,{sm:2,className:"table-header"},"Nominal Value"),r.a.createElement(g.a,{sm:2,className:"table-header"},"Nominal Rate"),r.a.createElement(g.a,{sm:1,className:"table-header"},"Nominal Accrued")),t.map(function(e,t){var a=new Date(e.time.toString());console.log(a.getDate(),a.getDate().toString().length);var n=(a.getMonth()+1).toString().length<2?"0"+(a.getMonth()+1):a.getMonth()+1,c=a.getDate().toString().length<2?"0"+a.getDate():a.getDate(),i=a.getFullYear()+"-"+n+"-"+c;return r.a.createElement(E.a,{key:t},r.a.createElement(g.a,{className:"cell-content",sm:2},i),r.a.createElement(g.a,{className:"cell-content",sm:1},e.type),r.a.createElement(g.a,{className:"cell-content",sm:2},e.payoff),r.a.createElement(g.a,{className:"cell-content",sm:2},e.currency),r.a.createElement(g.a,{className:"cell-content",sm:2},e.nominalValue),r.a.createElement(g.a,{className:"cell-content",sm:2},e.nominalRate),r.a.createElement(g.a,{className:"cell-content",sm:1},e.nominalAccrued))}))))))}}]),t}(n.PureComponent),T=a(151),P=(a(142),function(e){function t(){return Object(s.a)(this,t),Object(m.a)(this,Object(u.a)(t).apply(this,arguments))}return Object(d.a)(t,e),Object(o.a)(t,[{key:"render",value:function(){var e=this.props,t=(e.type,e.contracts);return r.a.createElement("div",{className:""},r.a.createElement(f.a,{className:"contract-grid"},r.a.createElement(E.a,null,t.map(function(e,t){return r.a.createElement(g.a,{key:"contract_col_".concat(t),sm:12,md:4,className:"contract-wrapper"},r.a.createElement("div",{className:"contract-content"},r.a.createElement(T.a,{className:"contract-link",to:"/form/".concat(e.name)},e.name),r.a.createElement("div",{className:"contract-description"},e.description)))}))))}}]),t}(n.PureComponent)),q=(a(143),function(e){function t(){var e,a;Object(s.a)(this,t);for(var n=arguments.length,r=new Array(n),c=0;c<n;c++)r[c]=arguments[c];return(a=Object(m.a)(this,(e=Object(u.a)(t)).call.apply(e,[this].concat(r)))).state={contracts:[]},a}return Object(d.a)(t,e),Object(o.a)(t,[{key:"componentDidMount",value:function(){var e=this;C.a.get("/data/forms.json").then(function(t){console.log(t.data),e.setState({contracts:t.data.contracts})})}},{key:"render",value:function(){var e=this.state.contracts;return r.a.createElement("div",null,r.a.createElement("div",{className:"section-intro"},"Choose a Contract Type from the list below in order to define and evaluate specific financial contracts:"),r.a.createElement("div",null,e.map(function(e,t){return r.a.createElement(P,{key:"_contract_".concat(t),type:e.type,contracts:e.items})})))}}]),t}(n.PureComponent)),B=function(e){function t(){return Object(s.a)(this,t),Object(m.a)(this,Object(u.a)(t).apply(this,arguments))}return Object(d.a)(t,e),Object(o.a)(t,[{key:"render",value:function(){return r.a.createElement("div",null,"FORM NOT FOUND")}}]),t}(n.PureComponent),x=(a(144),function(e){function t(){return Object(s.a)(this,t),Object(m.a)(this,Object(u.a)(t).apply(this,arguments))}return Object(d.a)(t,e),Object(o.a)(t,[{key:"render",value:function(){return r.a.createElement(v,null,r.a.createElement(p.a,null,r.a.createElement(h.a,{exact:!0,path:"/",render:function(e){return r.a.createElement(q,e)}}),r.a.createElement(h.a,{exact:!0,path:"/form/:id",render:function(e){return r.a.createElement(M,e)}}),r.a.createElement(h.a,{exact:!0,path:"/results",render:function(e){return r.a.createElement(S,e)}}),r.a.createElement(h.a,{component:B})))}}]),t}(n.Component)),Q=(a(145),document.getElementById("root"));i.a.render(r.a.createElement(l.a,null,r.a.createElement(x,null)),Q),"serviceWorker"in navigator&&navigator.serviceWorker.ready.then(function(e){e.unregister()})},65:function(e,t){e.exports="data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDAAIBAQIBAQICAgICAgICAwUDAwMDAwYEBAMFBwYHBwcGBwcICQsJCAgKCAcHCg0KCgsMDAwMBwkODw0MDgsMDAz/2wBDAQICAgMDAwYDAwYMCAcIDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAz/wAARCAAPAA8DASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD0n43/ALe17+1X+2VremeJ7b4i+KPDUepSaL4R8D+EfEUmhpfyrdeTHJcvGPMd2XfKG3oA4RT8mQPdf2Tv2m7r9lr/AIKF6x8J9H8W+KPGnwovpprK3i1y/bVLjQb2G1knkt4LkncyRSRywH7wIRclnRnbhvjd/wAEsPHX7Kn7cFx8S/CGlap4u8CalrN1rMMeh+JU8O6zpj3aTrJapcM4aMI0xxJFnfEWiITcWr37/gnd/wAE7dS8MeJtM8beNNLg037BFI9lYvcR3U0zyxtGXd4yVGFducksWzgV+95tieHFlbnQmnSdFKMbq/tX/d3jKPLrKyvzO7a0Py2l/a/16NJRftOdty6cnm9mnd2V9LbXP//Z"},68:function(e,t,a){e.exports=a(146)},73:function(e,t,a){},74:function(e,t,a){},75:function(e,t,a){}},[[68,1,2]]]);
//# sourceMappingURL=main.ce49ddae.chunk.js.map
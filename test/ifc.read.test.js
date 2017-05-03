var fs = require('fs');
var should = require('chai').shoud();
var read = require('./src/ifcUtils/ifc.read.js');

describe('Test the module which reads the and ifc file', () => {
	it('should test if the output is not empty', (done) => {
		read.readIFC(file, (content) => {
			content.should.not.be.empty();
		});
	});

});

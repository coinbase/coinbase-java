package com.coinbase.v1.deserializer;

import com.coinbase.v1.entity.Report;
import com.coinbase.v1.entity.ReportNode;
import com.fasterxml.jackson.databind.util.StdConverter;

import java.util.ArrayList;
import java.util.List;

public class ReportsLifter extends StdConverter<List<ReportNode>, List<Report>> {

    public List<Report> convert(List<ReportNode> nodes) {
	ArrayList<Report> result = new ArrayList<Report>();
	
	for (ReportNode node : nodes) {
	    result.add(node.getReport());
	}
	
	return result;
    }

}

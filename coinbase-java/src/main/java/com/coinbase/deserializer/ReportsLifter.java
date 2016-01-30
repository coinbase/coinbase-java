package com.coinbase.deserializer;

import com.coinbase.entity.Report;
import com.coinbase.entity.ReportNode;
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
